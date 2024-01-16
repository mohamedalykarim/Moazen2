package mohalim.islamic.alarm.alert.moazen.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.unit.Constraints
import mohalim.islamic.alarm.alert.moazen.core.receiver.AlarmReceiver
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.core.utils.TimesUtils
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale


class AlarmUtils {

    companion object {
        /**
         * alarm type example : "AZAN_TYPE_FAJR"
         * local date time example : "2023-12-10T23:37:00.908732"
         */
        private fun setAlarm(context: Context, alarmType: String, localDateTime: String) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("AZAN_TYPE", alarmType)
            }
            val time  = LocalDateTime.parse(localDateTime)
            val alarmTime = time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000L

            var alarmId = 1
            when (alarmType) {
                Constants.AZAN_TYPE_FAGR -> alarmId = 1
                Constants.AZAN_TYPE_PRE_FAGR -> alarmId = 11

                Constants.AZAN_TYPE_ZOHR -> alarmId = 2
                Constants.AZAN_TYPE_PRE_ZOHR -> alarmId = 22

                Constants.AZAN_TYPE_ASR -> alarmId = 3
                Constants.AZAN_TYPE_PRE_ASR -> alarmId = 33

                Constants.AZAN_TYPE_MAGHREB -> alarmId = 4
                Constants.AZAN_TYPE_PRE_MAGHREB -> alarmId = 44

                Constants.AZAN_TYPE_ESHA -> alarmId = 5
                Constants.AZAN_TYPE_PRE_ESHA -> alarmId = 55
            }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                PendingIntent.getBroadcast(
                    context,
                    alarmId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }

        fun setAlarmForFirstTime(context: Context, cityName: String){
            setAlarms(context, cityName)
        }

        fun setAlarms(context: Context, cityName: String){
            try {
                context.resources.assets.open("$cityName.json").bufferedReader().use {
                    val jsonString = it.readText()
                    val jsonObject = JSONObject(jsonString)
                    val dateJsonArray = jsonObject.getJSONArray(cityName)

                    /** date **/
                    val calender = Calendar.getInstance()

                    val dayofMonth = calender.get(Calendar.DAY_OF_MONTH)

                    val todayString: String = calender.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + "-" + if (dayofMonth < 10) "0$dayofMonth" else dayofMonth


                    for (i in 0 until dateJsonArray.length()) {
                        val item = dateJsonArray.getJSONObject(i)

                        if (item.has(todayString)){
                            val times = item.getJSONArray(todayString)
                            val currentMillisecond = calender.timeInMillis

                            /**
                             * Examle of date 2007-12-03T10:15:30:55.000000.
                             * **/
                            var date = TimesUtils.getLocalDateStringFromCalendar(calender, times.get(0).toString())
                            var calendarFromString = TimesUtils.localDateTimeStringToCalender(date)
                            if (currentMillisecond <= calendarFromString.timeInMillis){
                                setAlarm(context, Constants.AZAN_TYPE_FAGR, date)
                                calendarFromString.timeInMillis -= 15 * 60 * 1000
                                if (currentMillisecond <= calendarFromString.timeInMillis){
                                    date = TimesUtils.getLocalDateStringFromCalendar(calendarFromString, times.get(0).toString())
                                    setAlarm(context, Constants.AZAN_TYPE_PRE_FAGR, date)
                                }
                            }

                            date = TimesUtils.getLocalDateStringFromCalendar(calender, times.get(2).toString())
                            calendarFromString = TimesUtils.localDateTimeStringToCalender(date)
                            if (currentMillisecond <= calendarFromString.timeInMillis){
                                setAlarm(context, Constants.AZAN_TYPE_ZOHR, date)
                                calendarFromString.timeInMillis -= 15 * 60 * 1000
                                if (currentMillisecond <= calendarFromString.timeInMillis){
                                    date = TimesUtils.getLocalDateStringFromCalendar(calendarFromString, times.get(2).toString())
                                    setAlarm(context, Constants.AZAN_TYPE_PRE_ZOHR, date)
                                }
                            }

                            date = TimesUtils.getLocalDateStringFromCalendar(calender, times.get(3).toString())
                            calendarFromString = TimesUtils.localDateTimeStringToCalender(date)
                            if (currentMillisecond <= calendarFromString.timeInMillis){
                                setAlarm(context, Constants.AZAN_TYPE_ASR, date)
                                calendarFromString.timeInMillis -= 15 * 60 * 1000
                                if (currentMillisecond <= calendarFromString.timeInMillis){
                                    date = TimesUtils.getLocalDateStringFromCalendar(calendarFromString, times.get(3).toString())
                                    setAlarm(context, Constants.AZAN_TYPE_PRE_ASR, date)
                                }
                            }

                            date = TimesUtils.getLocalDateStringFromCalendar(calender, times.get(5).toString())
                            calendarFromString = TimesUtils.localDateTimeStringToCalender(date)
                            if (currentMillisecond <= calendarFromString.timeInMillis){
                                setAlarm(context, Constants.AZAN_TYPE_MAGHREB, date)
                                calendarFromString.timeInMillis -= 15 * 60 * 1000
                                if (currentMillisecond <= calendarFromString.timeInMillis){
                                    date = TimesUtils.getLocalDateStringFromCalendar(calendarFromString, times.get(5).toString())
                                    setAlarm(context, Constants.AZAN_TYPE_PRE_MAGHREB, date)
                                }
                            }

                            date = TimesUtils.getLocalDateStringFromCalendar(calender, times.get(6).toString())
                            calendarFromString = TimesUtils.localDateTimeStringToCalender(date)
                            if (currentMillisecond <= calendarFromString.timeInMillis){
                                setAlarm(context, Constants.AZAN_TYPE_ESHA, date)
                                calendarFromString.timeInMillis -= 15 * 60 * 1000
                                if (currentMillisecond <= calendarFromString.timeInMillis){
                                    date = TimesUtils.getLocalDateStringFromCalendar(calendarFromString, times.get(6).toString())
                                    Log.d("TAG", "setAlarms: pre Ishaa : " + date)
                                    setAlarm(context, Constants.AZAN_TYPE_PRE_ESHA, date)
                                }
                            }

//                            date = "2024-01-16T22:20:00"
//                            calendarFromString = TimesUtils.localDateTimeStringToCalender(date)
//
//                            if (currentMillisecond <= calendarFromString.timeInMillis){
//                                setAlarm(context, Constants.AZAN_TYPE_ESHA, date)
//                                calendarFromString.timeInMillis -= 15 * 60 * 1000
//                                if (currentMillisecond <= calendarFromString.timeInMillis){
//                                    date = TimesUtils.getLocalDateStringFromCalendar(calendarFromString, "22:05")
//                                    Log.d("TAG", "setAlarms: pre Ishaa fixed : " + date)
//                                    setAlarm(context, Constants.AZAN_TYPE_PRE_ESHA, date)
//                                }
//                            }
                        }

                    }

                }

            }catch (exception : Exception){
                Log.d("TAG", "setAlarmForFirstTime: "+ exception.message)
            }
        }




    }
}