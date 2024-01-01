package mohalim.islamic.alarm.alert.moazen.ui.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(val dataStore: DataStore<Preferences>) : ViewModel() {
    private val _showCityBottomSheet = MutableStateFlow(false)
    val showCityBottomSheet : StateFlow<Boolean> = _showCityBottomSheet.asStateFlow()

    private val _showPrayersBottomSheet = MutableStateFlow(false)
    val showPrayersBottomSheet : StateFlow<Boolean> = _showPrayersBottomSheet.asStateFlow()

    private val list : MutableList<String> = ArrayList()
    private val _prayersForToday = MutableStateFlow(list)
    val prayersForToday : StateFlow<MutableList<String>> = _prayersForToday.asStateFlow()

    private val _timer = MutableStateFlow("00:00:00")
    val timer : StateFlow<String> = _timer.asStateFlow()

    private val _nextPrayerType = MutableStateFlow("")
    val nextPrayerType : StateFlow<String> = _nextPrayerType.asStateFlow()

    private val _isNextDay = MutableStateFlow(false)
    val isNextDay : StateFlow<Boolean> = _isNextDay.asStateFlow()


    private val _currentCity = MutableStateFlow("")
    val currentCity : StateFlow<String> = _currentCity.asStateFlow()

    private val _isFagerAlertWork = MutableStateFlow(true)
    val isFagerAlertWork : StateFlow<Boolean> = _isFagerAlertWork.asStateFlow()

    private val _isDuhurAlertWork = MutableStateFlow(true)
    val isDuhurAlertWork : StateFlow<Boolean> = _isDuhurAlertWork.asStateFlow()

    private val _isAsrAlertWork = MutableStateFlow(true)
    val isAsrAlertWork : StateFlow<Boolean> = _isAsrAlertWork.asStateFlow()

    private val _isMaghribAlertWork = MutableStateFlow(true)
    val isMaghribAlertWork : StateFlow<Boolean> = _isMaghribAlertWork.asStateFlow()

    private val _isIshaaAlertWork = MutableStateFlow(true)
    val isIshaaAlertWork : StateFlow<Boolean> = _isIshaaAlertWork.asStateFlow()


    private lateinit var countDownTimer: CountDownTimer


    fun setShowCityBottomSheet(value : Boolean){
        _showCityBottomSheet.value = value
    }

    fun setShowPrayersBottomSheet(value : Boolean){
        _showPrayersBottomSheet.value = value
    }

    suspend fun observeIsFagrAlertsWorks(){
        viewModelScope.launch {
            PreferencesUtils.getIsFagrAlertWork(dataStore).collect{ _isFagerAlertWork.value = it }
        }
    }

    suspend fun observeIsDuhurAlertsWorks(){
        viewModelScope.launch {
            PreferencesUtils.getIsDuhurAlertWork(dataStore).collect{ _isDuhurAlertWork.value = it }
        }
    }

    suspend fun observeIsAsrAlertsWorks(){
        viewModelScope.launch {
            PreferencesUtils.getIsAsrAlertWork(dataStore).collect{ _isAsrAlertWork.value = it }
        }
    }

    suspend fun observeIsMaghribAlertsWorks(){
        viewModelScope.launch {
            PreferencesUtils.getIsMaghribAlertWork(dataStore).collect{ _isMaghribAlertWork.value = it }
        }
    }

    suspend fun observeIsIshaaAlertsWorks(){
        viewModelScope.launch {
            PreferencesUtils.getIsIshaaAlertWork(dataStore).collect{ _isIshaaAlertWork.value = it }
        }
    }

    suspend fun setIsFagrAlertWork(isWork : Boolean){
        viewModelScope.launch {
            PreferencesUtils.setIsFagrAlertWork(dataStore, isWork)
        }
    }

    suspend fun setIsDuhurAlertWork(isWork : Boolean){
        viewModelScope.launch {
            PreferencesUtils.setIsDuhurAlertWork(dataStore, isWork)
        }
    }

    suspend fun setIsAsrAlertWork(isWork : Boolean){
        viewModelScope.launch {
            PreferencesUtils.setIsAsrAlertWork(dataStore, isWork)
        }
    }

    suspend fun setIsMaghribAlertWork(isWork : Boolean){
        viewModelScope.launch {
            PreferencesUtils.setIsMaghribAlertWork(dataStore, isWork)
        }
    }

    suspend fun setIsIshaaAlertWork(isWork : Boolean){
        viewModelScope.launch {
            PreferencesUtils.setIsIshaaAlertWork(dataStore, isWork)
        }
    }

    suspend fun checkIfFirstOpen(context: Context) {
        viewModelScope.launch {
            PreferencesUtils.getIsFirstOpen(dataStore).collect{isFirstOpen ->
                if (isFirstOpen){
                    val permissionState = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                    // If the permission is not granted, request it.
                    if (permissionState == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            1
                        )
                    }
                    setShowCityBottomSheet(true)
                }
            }
        }
    }

    suspend fun getCurrentCityName(context : Context){
        viewModelScope.launch {
            PreferencesUtils.getCurrentCityName(dataStore).collect{
                if (it != ""){
                    _currentCity.value = it
                    var praysForToday = TimesUtils.getPraysForToday(context, it, 0)
                    var nextPray: NextPray? = TimesUtils.getNextPray(praysForToday, 0)

                    if (nextPray == null){
                        praysForToday = TimesUtils.getPraysForToday(context, it, 1)
                        nextPray = TimesUtils.getNextPray(praysForToday, 1)
                        _isNextDay.value = true
                    }

                    _prayersForToday.value = TimesUtils.getPrayersInTimeFormat(praysForToday)

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
                            viewModelScope.launch {
                                withContext(Dispatchers.IO){
                                    getCurrentCityName(context)
                                }
                            }
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