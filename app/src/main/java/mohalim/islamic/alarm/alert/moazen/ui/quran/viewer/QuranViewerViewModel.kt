package mohalim.islamic.alarm.alert.moazen.ui.quran.viewer

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class QuranViewerViewModel @Inject constructor() : ViewModel() {
    private val _zoomScale = MutableStateFlow(1f)
    val zoomScale : StateFlow<Float> = _zoomScale.asStateFlow()

    private val _zoomOffset = MutableStateFlow(Offset(1f,1f))
    val zoomOffset : StateFlow<Offset> = _zoomOffset.asStateFlow()

    fun setZoomScale(scale: Float){
        _zoomScale.value = scale
    }

    fun setZoomOffset(offset: Offset){
        _zoomOffset.value = offset
    }
}