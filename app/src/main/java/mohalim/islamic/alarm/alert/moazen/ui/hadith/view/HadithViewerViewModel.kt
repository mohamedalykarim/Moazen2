package mohalim.islamic.alarm.alert.moazen.ui.hadith.view

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import mohalim.islamic.alarm.alert.moazen.core.model.Hadith
import javax.inject.Inject


@HiltViewModel
class HadithViewerViewModel @Inject constructor() : ViewModel() {
    private val array = arrayListOf<Hadith>()
    private val _hadiths = MutableStateFlow(array)
    val hadiths = _hadiths.asStateFlow()

    fun setHadiths(hadiths: List<Hadith>) {
        array.clear()
        array.addAll(hadiths)
        _hadiths.value = array
    }


}