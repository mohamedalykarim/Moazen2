package mohalim.islamic.alarm.alert.moazen.ui.main

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.core.alarm.AlarmUtils
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.core.model.NextPray
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.core.utils.TimesUtils
import java.util.Calendar
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

    private val _midday = MutableStateFlow("")
    val midday : StateFlow<String> = _midday.asStateFlow()


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
            PreferencesUtils.observeIsFagrAlertWork(dataStore).collect{ _isFagerAlertWork.value = it }
        }
    }

    suspend fun observeIsDuhurAlertsWorks(){
        viewModelScope.launch {
            PreferencesUtils.observeIsDuhurAlertWork(dataStore).collect{ _isDuhurAlertWork.value = it }
        }
    }

    suspend fun observeIsAsrAlertsWorks(){
        viewModelScope.launch {
            PreferencesUtils.observeIsAsrAlertWork(dataStore).collect{ _isAsrAlertWork.value = it }
        }
    }

    suspend fun observeIsMaghribAlertsWorks(){
        viewModelScope.launch {
            PreferencesUtils.observeIsMaghribAlertWork(dataStore).collect{ _isMaghribAlertWork.value = it }
        }
    }

    suspend fun observeIsIshaaAlertsWorks(){
        viewModelScope.launch {
            PreferencesUtils.observeIsIshaaAlertWork(dataStore).collect{ _isIshaaAlertWork.value = it }
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
            PreferencesUtils.observeIsFirstOpen(dataStore).collect{isFirstOpen ->
                if (isFirstOpen){
                    setShowCityBottomSheet(true)
                }
            }
        }
    }

    suspend fun getCurrentCityName(context : Context){
        viewModelScope.launch {
            PreferencesUtils.observeCurrentCityName(dataStore).collect{
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

                    if (this@MainActivityViewModel::countDownTimer.isInitialized){
                        countDownTimer.cancel()

                    }

                    countDownTimer = object : CountDownTimer(nextPray.millisecondDifference, 1000){
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


                    /** Calculating Midday **/

                    val calendar = Calendar.getInstance()

                    if (_isNextDay.value) calendar.timeInMillis = calendar.timeInMillis + 24*60*60*1000
                    val year = calendar.get(Calendar.YEAR)
                    val monthLong = calendar.get(Calendar.MONTH) + 1
                    val dayLong = calendar.get(Calendar.DAY_OF_MONTH)
                    val hoursLong = calendar.get(Calendar.HOUR_OF_DAY)
                    val minutesLong = calendar.get(Calendar.MINUTE)

                    val month: String = if(monthLong < 10) "0$monthLong" else monthLong.toString()
                    val day : String = if(dayLong < 10) "0$dayLong" else dayLong.toString()

                    val hour : String = if(hoursLong < 10) "0$hoursLong" else hoursLong.toString()
                    val minutes : String = if(minutesLong < 10) "0$minutesLong" else minutesLong.toString()

                    val sunriseString = _prayersForToday.value[1].replace(" AM", "").replace(" PM", "")
                    val sunsetString = _prayersForToday.value[4].replace(" AM", "").replace(" PM", "")
                    val dateSunrise = "$year-$month-${day}T${sunriseString}:00"
                    val dateSunset = "$year-$month-${day}T${sunsetString}:00"

                    val calendarSunrise = TimesUtils.localDateTimeStringToCalender(dateSunrise)
                    val calendarSunset = TimesUtils.localDateTimeStringToCalender(dateSunset)
                    calendarSunset.timeInMillis = calendarSunset.timeInMillis + 12*60*60*1000

                    val millisecondDifference = calendarSunset.timeInMillis - calendarSunrise.timeInMillis
                    val calendarMidday = Calendar.getInstance()
                    calendarMidday.timeInMillis = calendarSunrise.timeInMillis + (millisecondDifference/2)

                    _midday.value = TimesUtils.getTimeFormat(calendarMidday)

                    var count = 1;
                    // reserve all times
                    repeat(24) {
                        if (count == 24) count = 0
                        var localDateString = if (count < 10)
                            TimesUtils.getLocalDateStringFromCalendar(calendar, "0"+count+":00")
                        else TimesUtils.getLocalDateStringFromCalendar(calendar, "${count}:00")

                        AlarmUtils.setRepeatedAlarm(
                            context,
                            Constants.RESERVE_ALL_TIMES,
                            1000+count,
                            localDateString
                        )

                        Log.d("TAG", "getCurrentCityName: Alarm reservation at : "+ localDateString)

                        count += 1
                    }



                    var localDateString = TimesUtils.getLocalDateStringFromCalendar(calendar, "${hour}:${minutes}")
                    AlarmUtils.setRepeatedAlarm(context, Constants.RESERVE_ALL_TIMES, 1001, localDateString)

                }
            }
        }
    }

    fun stopCounter(){
        if (this::countDownTimer.isInitialized) countDownTimer.cancel()
    }


}