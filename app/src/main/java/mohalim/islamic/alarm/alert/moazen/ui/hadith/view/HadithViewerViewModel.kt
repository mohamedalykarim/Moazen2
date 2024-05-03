package mohalim.islamic.alarm.alert.moazen.ui.hadith.view

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
import mohalim.islamic.alarm.alert.moazen.core.repository.RoomRepository
import mohalim.islamic.alarm.alert.moazen.core.room.dao.HadithDao
import mohalim.islamic.alarm.alert.moazen.core.room.entity.HadithEntity
import javax.inject.Inject


@HiltViewModel
class HadithViewerViewModel @Inject constructor(val roomRepository: RoomRepository) : ViewModel() {
    private val _currentHadith = MutableStateFlow(HadithEntity(1,"","",""))
    val currentHadith : StateFlow<HadithEntity> = _currentHadith.asStateFlow()

    fun setCurrentHadith(hadith : HadithEntity){
        _currentHadith.value = hadith
    }

    fun getCurrentHadithFromRoom(rawyfileName: String, page: Int) {
        viewModelScope.launch {
            roomRepository.getCurrentHadith(rawyfileName, page).collect{
                _currentHadith.value = it
            }
        }
    }
}