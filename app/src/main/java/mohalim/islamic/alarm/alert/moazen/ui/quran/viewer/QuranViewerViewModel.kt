package mohalim.islamic.alarm.alert.moazen.ui.quran.viewer

import androidx.compose.ui.geometry.Offset
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import javax.inject.Inject

@HiltViewModel
class QuranViewerViewModel @Inject constructor(val dataStore: DataStore<Preferences>) : ViewModel() {
    private val _zoomScale = MutableStateFlow(1f)
    val zoomScale : StateFlow<Float> = _zoomScale.asStateFlow()

    private val _zoomOffset = MutableStateFlow(Offset(1f,1f))
    val zoomOffset : StateFlow<Offset> = _zoomOffset.asStateFlow()

    private val _lastPage = MutableStateFlow(1)
    val lastPage : StateFlow<Int> = _lastPage.asStateFlow()

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
            runBlocking {
                withContext(Dispatchers.IO){
                    PreferencesUtils.setPageReference(dataStore, lastPage.value)
                }
            }
        }
    }
}