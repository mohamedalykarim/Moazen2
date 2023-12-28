package mohalim.islamic.alarm.alert.moazen.ui.main

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import mohalim.islamic.alarm.alert.moazen.core.utils.TimesUtils
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val dataStore: DataStore<Preferences>) : ViewModel() {
    private val _showCityBottomSheet = MutableStateFlow(false)
    val showCityBottomSheet : StateFlow<Boolean> = _showCityBottomSheet.asStateFlow()

    private val _praysForToday = MutableStateFlow(null)



    fun setShowCityBottomSheet(value : Boolean){
        _showCityBottomSheet.value = value
    }

    suspend fun checkIfFirstOpen() {
        viewModelScope.launch {
            PreferencesUtils.getIsFirstOpen(dataStore).collect{isFirstOpen ->
                Log.d("TAG", "onCreate: getIsFirstOpen : "+ isFirstOpen)
                if (isFirstOpen){
                    setShowCityBottomSheet(true)
                }
            }
        }
    }

    suspend fun getCurrentCityName(context : Context){
        viewModelScope.launch {
            PreferencesUtils.getCurrentCityName(dataStore).collect{
                if (it != ""){
                    val praysForToday = TimesUtils.getPraysForToday(context, it, 0)
                    Log.d("TAG", "getCurrentCityName: prayes" + praysForToday)
                }
            }
        }
    }


}