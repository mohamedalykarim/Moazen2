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

    private val _showAzanPerformerBottomSheet = MutableStateFlow(false)
    val showAzanPerformerBottomSheet : StateFlow<Boolean> = _showAzanPerformerBottomSheet.asStateFlow()

    private val _azanPerformerFagr = MutableStateFlow(0)
    val azanPerformerFagr : StateFlow<Int> = _azanPerformerFagr.asStateFlow()

    private val _azanPerformerDuhur = MutableStateFlow(0)
    val azanPerformerDuhur : StateFlow<Int> = _azanPerformerDuhur.asStateFlow()

    private val _azanPerformerAsr = MutableStateFlow(0)
    val azanPerformerAsr : StateFlow<Int> = _azanPerformerAsr.asStateFlow()

    private val _azanPerformerMaghrib = MutableStateFlow(0)
    val azanPerformerMaghrib : StateFlow<Int> = _azanPerformerMaghrib.asStateFlow()

    private val _azanPerformerIshaa = MutableStateFlow(0)
    val azanPerformerIshaa : StateFlow<Int> = _azanPerformerIshaa.asStateFlow()



    suspend fun getCurrentCityName() {
        viewModelScope.launch {
            PreferencesUtils.getCurrentCityName(dataStore).collect{
                _currentCity.value = it
            }
        }

    }

    suspend fun getAzanPerformerFagr() {
        viewModelScope.launch {
            PreferencesUtils.observeDefaultAzanTypeFagr(dataStore).collect{
                _azanPerformerFagr.value = it
            }
        }

    }

    suspend fun getAzanPerformerDuhur() {
        viewModelScope.launch {
            PreferencesUtils.observeDefaultAzanTypeDuhur(dataStore).collect{
                _azanPerformerDuhur.value = it
            }
        }

    }

    suspend fun getAzanPerformerAsr() {
        viewModelScope.launch {
            PreferencesUtils.observeDefaultAzanTypeAsr(dataStore).collect{
                _azanPerformerAsr.value = it
            }
        }

    }

    suspend fun getAzanPerformerMaghrib() {
        viewModelScope.launch {
            PreferencesUtils.observeDefaultAzanTypeMaghrib(dataStore).collect{
                _azanPerformerMaghrib.value = it
            }
        }

    }

    suspend fun getAzanPerformerIshaa() {
        viewModelScope.launch {
            PreferencesUtils.observeDefaultAzanTypeIshaa(dataStore).collect{
                _azanPerformerIshaa.value = it
            }
        }

    }

    fun setShowCityBottomSheet(value : Boolean){
        _showCityBottomSheet.value = value
    }

    fun setShowAzanPerformerSheet(value : Boolean){
        _showAzanPerformerBottomSheet.value = value
    }
}