package mohalim.islamic.alarm.alert.moazen.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import mohalim.islamic.alarm.alert.moazen.core.reciever.AlarmReciever
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date


class AlarmUtils {

    companion object {
        /**
         * alarm type example : "AZAN_TYPE_FAJR"
         * local date time example : "2023-12-10T23:37:00.908732"
         */
        fun setAlarm(context: Context, alarmType: String, localDateTime: String) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            val intent = Intent(context, AlarmReciever::class.java).apply {
                putExtra("AZAN_TYPE", alarmType)
            }
            val time  = LocalDateTime.parse(localDateTime)
            val alarmTime = time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000L

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                PendingIntent.getBroadcast(
                    context,
                    1,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }

        fun setAlarmForFirstTime(context: Context, cityName: String){
            try {
                context.resources.assets.open("$cityName.json").bufferedReader().use {
                    val jsonString = it.readText()
                    val jsonObject = JSONObject(jsonString)
                    val dateJsonArray = jsonObject.getJSONArray("$cityName")


                    /** date **/
                    val calender = Calendar.getInstance()
                    Log.d("TAG", "setAlarmForFirstTime: "+calender.get(Calendar.DAY_OF_YEAR))
                }

            }catch (exception : Exception){
                Log.d("TAG", "setAlarmForFirstTime: "+ exception.message)
            }
        }
    }
}