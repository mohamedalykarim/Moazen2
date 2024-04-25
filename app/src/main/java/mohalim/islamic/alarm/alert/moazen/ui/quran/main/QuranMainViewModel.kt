package mohalim.islamic.alarm.alert.moazen.ui.quran.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.core.model.Surah
import mohalim.islamic.alarm.alert.moazen.core.utils.Utils
import javax.inject.Inject

@HiltViewModel
class QuranMainViewModel @Inject constructor() : ViewModel() {
    val _allSurah = MutableStateFlow<MutableList<Surah>>(ArrayList())
    val allSurah = _allSurah.asStateFlow()


    fun startToGetAllSurahMetaData(context : Context) {
        viewModelScope.launch {
            val all = Utils.getAllSurahMetaData(context)
            _allSurah.value.clear()
            _allSurah.value.addAll(all)
        }
    }
}