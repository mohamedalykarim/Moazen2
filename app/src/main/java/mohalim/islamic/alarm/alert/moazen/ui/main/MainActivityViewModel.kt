package mohalim.islamic.alarm.alert.moazen.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    private val _showCityBottomSheet = MutableStateFlow(false)
    val showCityBottomSheet : StateFlow<Boolean> = _showCityBottomSheet.asStateFlow()

    fun setShowCityBottomSheet(value : Boolean){
        _showCityBottomSheet.value = value
    }


}