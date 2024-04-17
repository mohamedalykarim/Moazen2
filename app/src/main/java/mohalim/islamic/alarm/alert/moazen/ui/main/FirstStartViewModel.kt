package mohalim.islamic.alarm.alert.moazen.ui.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FirstStartViewModel @Inject constructor(val dataStore: DataStore<Preferences>) : ViewModel() {
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

}