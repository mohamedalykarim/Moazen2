package mohalim.islamic.alarm.alert.moazen.ui.quran.viewer

import androidx.compose.ui.geometry.Offset
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.core.model.quran.PageApi
import mohalim.islamic.alarm.alert.moazen.core.network.interfaces.QuranApiInterface
import mohalim.islamic.alarm.alert.moazen.core.room.dao.QuranDao
import mohalim.islamic.alarm.alert.moazen.core.room.entity.QuranPageEntity
import javax.inject.Inject

@HiltViewModel
class QuranViewerViewModel @Inject constructor(
    val dataStore: DataStore<Preferences>,
    private val quranApi: QuranApiInterface,
    private val quranDao: QuranDao
) : ViewModel() {
    private val _zoomScale = MutableStateFlow(1f)
    val zoomScale : StateFlow<Float> = _zoomScale.asStateFlow()

    private val _zoomOffset = MutableStateFlow(Offset(1f,1f))
    val zoomOffset : StateFlow<Offset> = _zoomOffset.asStateFlow()

    private val _lastPage = MutableStateFlow(1)
    val lastPage : StateFlow<Int> = _lastPage.asStateFlow()

    private val _pageContent = MutableStateFlow<Map<Int, PageApi>>(emptyMap())
    val pageContent: StateFlow<Map<Int, PageApi>> = _pageContent.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _downloadProgress = MutableStateFlow(0f)
    val downloadProgress = _downloadProgress.asStateFlow()

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading = _isDownloading.asStateFlow()

    private val _showDownloadPrompt = MutableStateFlow(false)
    val showDownloadPrompt = _showDownloadPrompt.asStateFlow()

    private val _quranFont = MutableStateFlow("mushaf")
    val quranFont: StateFlow<String> = _quranFont.asStateFlow()

    init {
        checkOfflineData()
        observeQuranFont()
    }

    private fun observeQuranFont() {
        viewModelScope.launch {
            PreferencesUtils.observeQuranFont(dataStore).collect {
                _quranFont.value = it
            }
        }
    }

    private fun checkOfflineData() {
        viewModelScope.launch {
            val count = quranDao.getPagesCount()
            if (count < 604) {
                _showDownloadPrompt.value = true
            }
        }
    }

    fun dismissDownloadPrompt() {
        _showDownloadPrompt.value = false
    }

    fun startFullDownload() {
        _showDownloadPrompt.value = false
        _isDownloading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 1..604) {
                try {
                    val existing = quranDao.getPage(i)
                    if (existing == null) {
                        val response = quranApi.getPage(i)
                        if (response.code == 200) {
                            quranDao.insertPage(QuranPageEntity(i, Gson().toJson(response.data)))
                        }
                    }
                    _downloadProgress.value = i / 604f
                } catch (e: Exception) {
                    // Log error
                }
            }
            _isDownloading.value = false
        }
    }

    fun setZoomScale(scale: Float){
        _zoomScale.value = scale
    }

    fun setZoomOffset(offset: Offset){
        _zoomOffset.value = offset
    }

    fun setLastPage(page: Int){
        _lastPage.value = page
    }

    fun setPreferencesPageReference() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                PreferencesUtils.setPageReference(dataStore, lastPage.value)
            }
        }
    }

    fun fetchPage(pageNumber: Int) {
        if (_pageContent.value.containsKey(pageNumber)) return

        viewModelScope.launch {
            _isLoading.value = true
            // Try Offline first
            val offlinePage = withContext(Dispatchers.IO) { quranDao.getPage(pageNumber) }
            if (offlinePage != null) {
                val pageData = Gson().fromJson(offlinePage.data, PageApi::class.java)
                val currentMap = _pageContent.value.toMutableMap()
                currentMap[pageNumber] = pageData
                _pageContent.value = currentMap
                _isLoading.value = false
            } else {
                // Fetch Online
                try {
                    val response = quranApi.getPage(pageNumber)
                    if (response.code == 200) {
                        val currentMap = _pageContent.value.toMutableMap()
                        currentMap[pageNumber] = response.data
                        _pageContent.value = currentMap
                        
                        // Cache it
                        withContext(Dispatchers.IO) {
                            quranDao.insertPage(QuranPageEntity(pageNumber, Gson().toJson(response.data)))
                        }
                    }
                } catch (e: Exception) {
                    // Handle error
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
}
