package mohalim.islamic.alarm.alert.moazen.ui.quran.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.core.model.Page
import mohalim.islamic.alarm.alert.moazen.core.model.quran.SurahApi
import mohalim.islamic.alarm.alert.moazen.core.network.interfaces.QuranApiInterface
import javax.inject.Inject

@HiltViewModel
class QuranMainViewModel @Inject constructor(private val quranApi: QuranApiInterface) : ViewModel() {
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
                val response = quranApi.getSurahList()
                Log.d("QuranMainViewModel", "fetchSurahList: ${response.data}")
                if (response.code == 200) {
                    _allSurah.value = response.data
                }
            } catch (e: Exception) {
                // Handle error
                Log.e("QuranMainViewModel", "fetchSurahList: ${e.message}")
            }
        }
    }
}
