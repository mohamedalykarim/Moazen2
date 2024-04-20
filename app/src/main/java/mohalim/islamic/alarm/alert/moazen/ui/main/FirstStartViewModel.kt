package mohalim.islamic.alarm.alert.moazen.ui.main

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
import mohalim.islamic.alarm.alert.moazen.core.room.dao.AzkarDao
import mohalim.islamic.alarm.alert.moazen.core.room.entity.AzkarEntity
import javax.inject.Inject

@HiltViewModel
class FirstStartViewModel @Inject constructor(val dataStore: DataStore<Preferences>, val azkarDao: AzkarDao) : ViewModel() {
    private val _autoStartPermissionGranted = MutableStateFlow(false)
    val autoStartPermissionGranted : StateFlow<Boolean> = _autoStartPermissionGranted.asStateFlow()


    private val _notificationPermissionGranted = MutableStateFlow(false)
    val notificationPermissionGranted : StateFlow<Boolean> = _notificationPermissionGranted.asStateFlow()

    private val _scheduleAlarmPermissionGranted = MutableStateFlow(false)
    val scheduleAlarmPermissionGranted : StateFlow<Boolean> = _scheduleAlarmPermissionGranted.asStateFlow()


    fun setAutoStartPermissionGranted(boolean: Boolean){
        _autoStartPermissionGranted.value = boolean
    }

    fun setNotificationPermissionGranted(boolean: Boolean){
        _notificationPermissionGranted.value = boolean
    }

    fun setScheduleAlarmPermissionGranted(boolean: Boolean){
        _scheduleAlarmPermissionGranted.value = boolean
    }

    fun addDefaultAzkar(){
        viewModelScope.launch {
            runBlocking { withContext(Dispatchers.IO){
                azkarDao.addNew(AzkarEntity(1,"لا اله الا الله", 0))
                azkarDao.addNew(AzkarEntity(2,"سبحان الله", 0))
                azkarDao.addNew(AzkarEntity(3,"الحمدلله", 0))
                azkarDao.addNew(AzkarEntity(4,"الله اكبر", 0))
                azkarDao.addNew(AzkarEntity(5,"استغفر الله العظيم واتوب اليه", 0))
                azkarDao.addNew(AzkarEntity(6,"سبحان الله وبحمده عدد خلقه ورضا نفسه وزنة عرشه ومداد كلماته", 0))
            } }
        }
    }

}