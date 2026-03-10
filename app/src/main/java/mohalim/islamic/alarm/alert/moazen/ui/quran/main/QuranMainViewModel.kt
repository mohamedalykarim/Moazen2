package mohalim.islamic.alarm.alert.moazen.ui.quran.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.core.model.Page
import mohalim.islamic.alarm.alert.moazen.core.model.quran.SurahApi
import mohalim.islamic.alarm.alert.moazen.core.network.interfaces.QuranApiInterface
import mohalim.islamic.alarm.alert.moazen.core.room.dao.QuranDao
import mohalim.islamic.alarm.alert.moazen.core.room.entity.SurahEntity
import javax.inject.Inject

@HiltViewModel
class QuranMainViewModel @Inject constructor(
    private val quranApi: QuranApiInterface,
    private val quranDao: QuranDao
) : ViewModel() {
    private val _allSurah = MutableStateFlow<List<SurahApi>>(emptyList())
    val allSurah = _allSurah.asStateFlow()

    private val _pageNumberReference = MutableStateFlow(1)
    val pageNumberReference : StateFlow<Int> = _pageNumberReference.asStateFlow()

    private val _pageReference = MutableStateFlow<Page?>(null)
    val pageReference : StateFlow<Page?> = _pageReference.asStateFlow()


    fun setPageNumberReference(page : Int){
        _pageNumberReference.value = page
    }

    fun setPageReference(page : Page){
        _pageReference.value = page
    }

    fun fetchSurahList() {
        viewModelScope.launch {
            try {
                // Try to get from database first
                val cachedSurahs = withContext(Dispatchers.IO) { quranDao.getAllSurahs() }
                if (cachedSurahs.isNotEmpty()) {
                    _allSurah.value = cachedSurahs.map {
                        SurahApi(it.number, it.name, it.englishName, it.englishNameTranslation, it.revelationType, it.numberOfAyahs)
                    }
                } else {
                    // Fetch from API
                    val response = quranApi.getSurahList()
                    if (response.code == 200) {
                        _allSurah.value = response.data
                        // Cache in database
                        withContext(Dispatchers.IO) {
                            quranDao.insertSurahs(response.data.map {
                                SurahEntity(it.number, it.name, it.englishName, it.englishNameTranslation, it.revelationType, it.numberOfAyahs)
                            })
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("QuranMainViewModel", "fetchSurahList: ${e.message}")
            }
        }
    }
}
