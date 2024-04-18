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


    fun setAutoStartPermissionGranted(boolean: Boolean){
        _autoStartPermissionGranted.value = boolean
    }

    fun setNotificationPermissionGranted(boolean: Boolean){
        _notificationPermissionGranted.value = boolean
    }

    fun addDefaultAzkar(){
        viewModelScope.launch {
            runBlocking { withContext(Dispatchers.IO){
                azkarDao.addNew(AzkarEntity(null,"لا اله الا الله", 0))
                azkarDao.addNew(AzkarEntity(null,"سبحان الله", 0))
                azkarDao.addNew(AzkarEntity(null,"الحمدلله", 0))
                azkarDao.addNew(AzkarEntity(null,"الله اكبر", 0))
                azkarDao.addNew(AzkarEntity(null,"استغفر الله العظيم واتوب اليه", 0))
                azkarDao.addNew(AzkarEntity(null,"سبحان الله وبحمده عدد خلقه ورضا نفسه ومداد كلماته", 0))
            } }
        }
    }

}