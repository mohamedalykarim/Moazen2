package mohalim.islamic.alarm.alert.moazen.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import mohalim.islamic.alarm.alert.moazen.core.receiver.AlarmReceiver
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
                "AZAN_TYPE_FAGR" -> alarmId = 1
                "AZAN_TYPE_ZOHR" -> alarmId = 2
                "AZAN_TYPE_ASR" -> alarmId = 3
                "AZAN_TYPE_MAGHREB" -> alarmId = 4
                "AZAN_TYPE_ESHA" -> alarmId = 5
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

                    Log.d("TAG", "setAlarms: "+todayString)

                    for (i in 0 until dateJsonArray.length()) {
                        val item = dateJsonArray.getJSONObject(i)

                        if (item.has(todayString)){
                            val times = item.getJSONArray(todayString)
                            val year = calender.get(Calendar.YEAR)
                            val monthLong = calender.get(Calendar.MONTH) + 1
                            val dayLong = calender.get(Calendar.DAY_OF_MONTH)
                            val currentMillisecond = calender.timeInMillis

                            var month = ""
                            var day = ""
                            month = if(monthLong < 10) "0$monthLong" else monthLong.toString()
                            day = if(dayLong < 10) "0$dayLong" else dayLong.toString()



                            /**
                             * Examle of date 2007-12-03T10:15:30:55.000000.
                             * **/
                            var date = "$year-$month-${day}T${times.get(0)}:00"
                            Log.d("TAG", "setAlarms: "+date)
                            var dateMillisecond = TimesUtils.localDateTimeStringToCalender(date).timeInMillis
                            if (currentMillisecond <= dateMillisecond){
                                setAlarm(context, "AZAN_TYPE_FAGR", date)
                            }

                            date = "$year-$month-${day}T${times.get(2)}:00"
                            dateMillisecond = TimesUtils.localDateTimeStringToCalender(date).timeInMillis
                            if (currentMillisecond <= dateMillisecond){
                                setAlarm(context, "AZAN_TYPE_ZOHR", date)
                           }

                            date = "$year-$month-${day}T${times.get(3)}:00"
                            dateMillisecond = TimesUtils.localDateTimeStringToCalender(date).timeInMillis
                            if (currentMillisecond <= dateMillisecond){
                                setAlarm(context, "AZAN_TYPE_ASR", date)
                            }

                            date = "$year-$month-${day}T${times.get(5)}:00"
                            dateMillisecond = TimesUtils.localDateTimeStringToCalender(date).timeInMillis
                            if (currentMillisecond <= dateMillisecond){
                                setAlarm(context, "AZAN_TYPE_MAGHREB", date)
                                Log.d("TAG", "setAlarms: "+ date)
                            }

                            date = "$year-$month-${day}T${times.get(6)}:00"
                            dateMillisecond = TimesUtils.localDateTimeStringToCalender(date).timeInMillis
                            if (currentMillisecond <= dateMillisecond){
                                setAlarm(context, "AZAN_TYPE_ESHA", date)
                                Log.d("TAG", "setAlarms: "+ date)
                            }

//                            date = "2024-01-08T10:50:00"
//                            dateMillisecond = TimesUtils.localDateTimeStringToCalender(date).timeInMillis
//                            if (currentMillisecond <= dateMillisecond){
//                                setAlarm(context, "AZAN_TYPE_ESHA", date)
//                            }
//                            Log.d("TAG", "setAlarms: "+date)
                        }

                    }

                }

            }catch (exception : Exception){
                Log.d("TAG", "setAlarmForFirstTime: "+ exception.message)
            }
        }




    }
}