package mohalim.islamic.alarm.alert.moazen.ui.main

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.core.model.NextPray
import mohalim.islamic.alarm.alert.moazen.core.service.TimerWorker
import mohalim.islamic.alarm.alert.moazen.core.utils.TimesUtils
import org.json.JSONArray
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val dataStore: DataStore<Preferences>) : ViewModel() {
    private val _showCityBottomSheet = MutableStateFlow(false)
    val showCityBottomSheet : StateFlow<Boolean> = _showCityBottomSheet.asStateFlow()

    private val _praysForToday = MutableStateFlow(JSONArray())
    val praysForToday : StateFlow<JSONArray> = _praysForToday.asStateFlow()

    private val _timer = MutableStateFlow("00:00:00")
    val timer : StateFlow<String> = _timer.asStateFlow()

    private val _nextPrayerType = MutableStateFlow("")
    val nextPrayerType : StateFlow<String> = _nextPrayerType.asStateFlow()

    private lateinit var countDownTimer: CountDownTimer


    fun setShowCityBottomSheet(value : Boolean){
        _showCityBottomSheet.value = value
    }

    suspend fun checkIfFirstOpen() {
        viewModelScope.launch {
            PreferencesUtils.getIsFirstOpen(dataStore).collect{isFirstOpen ->
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
                    var praysForToday = TimesUtils.getPraysForToday(context, it, 0)
                    var nextPray: NextPray? = TimesUtils.getNextPray(praysForToday, 0)

                    if (nextPray == null){
                        praysForToday = TimesUtils.getPraysForToday(context, it, 1)
                        nextPray = TimesUtils.getNextPray(praysForToday, 1)
                    }

                    Log.d("TAG", "getCurrentCityName: prayers" + praysForToday)

                    _nextPrayerType.value = nextPray!!.azanType

                    withContext(Dispatchers.IO){
                        val setAlarmsRequest: PeriodicWorkRequest =
                            PeriodicWorkRequest
                                .Builder(
                                    TimerWorker::class.java,
                                    1,
                                    TimeUnit.HOURS
                                )
                                .build()


                        WorkManager
                            .getInstance(context)
                            .enqueueUniquePeriodicWork(
                                "TimerWorker",
                                ExistingPeriodicWorkPolicy.UPDATE,
                                setAlarmsRequest
                            )
                    }

                    val countDownTimer = object : CountDownTimer(nextPray.millisecondDifference, 1000){
                        override fun onTick(millisUntilFinished: Long) {
                            val (hours, minutes, seconds) = TimesUtils.convertMillisecondsToTime(millisUntilFinished)

                            _timer.value = "$hours:$minutes:$seconds"
                        }

                        override fun onFinish() {
                            _timer.value = "00:00:00"
                        }

                    }

                    countDownTimer.start()


                }
            }
        }
    }

    fun stopCounter(){
        if (this::countDownTimer.isInitialized) countDownTimer.cancel()
    }


}