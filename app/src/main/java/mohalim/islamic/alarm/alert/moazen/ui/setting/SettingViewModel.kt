package mohalim.islamic.alarm.alert.moazen.ui.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(val dataStore: DataStore<Preferences>) : ViewModel() {
    private val _currentCity = MutableStateFlow("")
    val currentCity : StateFlow<String> = _currentCity.asStateFlow()

    private val _showCityBottomSheet = MutableStateFlow(false)
    val showCityBottomSheet : StateFlow<Boolean> = _showCityBottomSheet.asStateFlow()


    suspend fun getCurrentCityName(context : Context) {
        viewModelScope.launch {
            PreferencesUtils.getCurrentCityName(dataStore).collect{
                _currentCity.value = it
            }
        }

    }

    fun setShowCityBottomSheet(value : Boolean){
        _showCityBottomSheet.value = value
    }
}