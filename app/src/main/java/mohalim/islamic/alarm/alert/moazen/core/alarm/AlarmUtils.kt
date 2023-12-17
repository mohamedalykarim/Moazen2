package mohalim.islamic.alarm.alert.moazen.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import mohalim.islamic.alarm.alert.moazen.core.reciever.AlarmReciever
import java.time.LocalDateTime
import java.time.ZoneId


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
    }
}