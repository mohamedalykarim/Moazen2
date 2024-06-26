package mohalim.islamic.alarm.alert.moazen.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
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
        private fun setAlarm(context: Context, alarmType: String, localDateTime: String, isSummerTimeOn: Boolean) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("AZAN_TYPE", alarmType)
            }
            val time  = LocalDateTime.parse(localDateTime)
            var alarmTime = time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000L

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

            if (isSummerTimeOn) alarmTime += 60 * 60 * 1000


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

        public fun setRepeatedAlarm(context: Context, alarmType: String, alarmId: Int,  localDateTime: String) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("AZAN_TYPE", alarmType)
            }
            val time  = LocalDateTime.parse(localDateTime)
            val alarmTime = time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000L

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                24*60*60*1000,
                PendingIntent.getBroadcast(
                    context,
                    alarmId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }


        suspend fun setAlarmForFirstTime(context: Context, cityName: String, dataStore : DataStore<Preferences>){
            setAlarms(context, cityName, dataStore)
        }

        suspend fun setAlarms(context: Context, cityName: String, dataStore : DataStore<Preferences>) {
            val isSummerTimeOn = PreferencesUtils.getSummerTime(dataStore)

            try {
                context.resources.assets.open("$cityName.json").bufferedReader().use {
                    val jsonString = it.readText()
                    val jsonObject = JSONObject(jsonString)
                    val dateJsonArray = jsonObject.getJSONArray(cityName)

                    /** date **/
                    val calender = Calendar.getInstance()

                    val dayofMonth = calender.get(Calendar.DAY_OF_MONTH)

                    val todayString: String = calender.getDisplayName(
                        Calendar.MONTH,
                        Calendar.LONG,
                        Locale.ENGLISH
                    )!! + "-" + if (dayofMonth < 10) "0$dayofMonth" else dayofMonth


                    for (i in 0 until dateJsonArray.length()) {
                        val item = dateJsonArray.getJSONObject(i)

                        if (item.has(todayString)) {
                            val times = item.getJSONArray(todayString)
                            var currentMillisecond = calender.timeInMillis

                            if (isSummerTimeOn) currentMillisecond -= 60*60*1000

                            /**
                             * Examle of date 2007-12-03T10:15:30:55.000000.
                             * **/
                            var localDateString = TimesUtils.getLocalDateStringFromCalendar(
                                calender,
                                times.get(0).toString()
                            )
                            var calendarFromlocalDateString =
                                TimesUtils.localDateTimeStringToCalender(localDateString)

                            if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
                                setAlarm(
                                    context,
                                    Constants.AZAN_TYPE_FAGR,
                                    localDateString,
                                    isSummerTimeOn
                                )
                                calendarFromlocalDateString.timeInMillis =
                                    calendarFromlocalDateString.timeInMillis - 15 * 60 * 1000
                                if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
                                    localDateString =
                                        TimesUtils.getLocalDateStringFromCalendarHourAndMinutes(
                                            calendarFromlocalDateString
                                        )
                                    setAlarm(
                                        context,
                                        Constants.AZAN_TYPE_PRE_FAGR,
                                        localDateString,
                                        isSummerTimeOn
                                    )
                                }
                            }

                            localDateString = TimesUtils.getLocalDateStringFromCalendar(
                                calender,
                                times.get(2).toString()
                            )
                            calendarFromlocalDateString =
                                TimesUtils.localDateTimeStringToCalender(localDateString)

                            if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
                                setAlarm(
                                    context,
                                    Constants.AZAN_TYPE_ZOHR,
                                    localDateString,
                                    isSummerTimeOn
                                )
                                calendarFromlocalDateString.timeInMillis =
                                    calendarFromlocalDateString.timeInMillis - 15 * 60 * 1000
                                if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
                                    localDateString =
                                        TimesUtils.getLocalDateStringFromCalendarHourAndMinutes(
                                            calendarFromlocalDateString
                                        )
                                    setAlarm(
                                        context,
                                        Constants.AZAN_TYPE_PRE_ZOHR,
                                        localDateString,
                                        isSummerTimeOn
                                    )
                                }
                            }

                            localDateString = TimesUtils.getLocalDateStringFromCalendar(
                                calender,
                                times.get(3).toString()
                            )
                            calendarFromlocalDateString =
                                TimesUtils.localDateTimeStringToCalender(localDateString)

                            if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
                                setAlarm(
                                    context,
                                    Constants.AZAN_TYPE_ASR,
                                    localDateString,
                                    isSummerTimeOn
                                )
                                calendarFromlocalDateString.timeInMillis =
                                    calendarFromlocalDateString.timeInMillis - 15 * 60 * 1000

                                if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
                                    localDateString =
                                        TimesUtils.getLocalDateStringFromCalendarHourAndMinutes(
                                            calendarFromlocalDateString
                                        )
                                    setAlarm(
                                        context,
                                        Constants.AZAN_TYPE_PRE_ASR,
                                        localDateString,
                                        isSummerTimeOn
                                    )
                                }
                            }

                            localDateString = TimesUtils.getLocalDateStringFromCalendar(
                                calender,
                                times.get(5).toString()
                            )
                            calendarFromlocalDateString =
                                TimesUtils.localDateTimeStringToCalender(localDateString)

                            if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
                                setAlarm(
                                    context,
                                    Constants.AZAN_TYPE_MAGHREB,
                                    localDateString,
                                    isSummerTimeOn
                                )
                                calendarFromlocalDateString.timeInMillis =
                                    calendarFromlocalDateString.timeInMillis - 15 * 60 * 1000

                                if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
                                    localDateString =
                                        TimesUtils.getLocalDateStringFromCalendarHourAndMinutes(
                                            calendarFromlocalDateString
                                        )
                                    setAlarm(
                                        context,
                                        Constants.AZAN_TYPE_PRE_MAGHREB,
                                        localDateString,
                                        isSummerTimeOn
                                    )
                                }
                            }

                            localDateString = TimesUtils.getLocalDateStringFromCalendar(
                                calender,
                                times.get(6).toString()
                            )
                            calendarFromlocalDateString =
                                TimesUtils.localDateTimeStringToCalender(localDateString)

                            if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
                                setAlarm(
                                    context,
                                    Constants.AZAN_TYPE_ESHA,
                                    localDateString,
                                    isSummerTimeOn
                                )
                                calendarFromlocalDateString.timeInMillis =
                                    calendarFromlocalDateString.timeInMillis - 15 * 60 * 1000

                                if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
                                    localDateString =
                                        TimesUtils.getLocalDateStringFromCalendarHourAndMinutes(
                                            calendarFromlocalDateString
                                        )
                                    setAlarm(
                                        context,
                                        Constants.AZAN_TYPE_PRE_ESHA,
                                        localDateString,
                                        isSummerTimeOn
                                    )
                                }
                            }

//                            localDateString = "2024-04-23T10:00:00"
//                            calendarFromlocalDateString =
//                                TimesUtils.localDateTimeStringToCalender(localDateString)
//
//                            if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
//                                setAlarm(
//                                    context,
//                                    Constants.AZAN_TYPE_ESHA,
//                                    localDateString,
//                                    isSummerTimeOn
//                                )
//
//                                calendarFromlocalDateString.timeInMillis =
//                                    calendarFromlocalDateString.timeInMillis - 15 * 60 * 1000
//
//
//                                if (currentMillisecond <= calendarFromlocalDateString.timeInMillis) {
//                                    localDateString = TimesUtils.getLocalDateStringFromCalendar(
//                                        calendarFromlocalDateString,
//                                        "09:51"
//                                    )
//
//                                    setAlarm(
//                                        context,
//                                        Constants.AZAN_TYPE_PRE_ESHA,
//                                        localDateString,
//                                        isSummerTimeOn
//                                    )
//                                }
//                            }

                        }

                    }

                }


                /**
                 * Friday Notification
                 */

                val alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("AZAN_TYPE", Constants.ALKAHF_READ_REMINDER)
                }

                val calendar = Calendar.getInstance()
                val currentMillisecond = calendar.timeInMillis

                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
                calendar.set(Calendar.HOUR_OF_DAY, 10)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)

                if (currentMillisecond <= calendar.timeInMillis) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        PendingIntent.getBroadcast(
                            context,
                            Constants.ALARM_ID_ALKAHF_REMINDER,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                    )
                }


            }catch (exception : Exception){
                Log.d("TAG", "setAlarmForFirstTime: " + exception.message)
            }
        }

    }


}















