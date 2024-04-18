package mohalim.islamic.alarm.alert.moazen.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.core.alarm.AlarmUtils
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.core.utils.TimesUtils
import java.util.Calendar
import javax.inject.Inject


@AndroidEntryPoint
class StartUpReceiver : BroadcastReceiver() {
    @Inject
    lateinit var dataStore: DataStore<Preferences>;

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("TAG", "onReceive: BOOT_COMPLETED")

        val calendar = Calendar.getInstance()

        val hoursLong = calendar.get(Calendar.HOUR_OF_DAY)
        val minutesLong = calendar.get(Calendar.MINUTE)

        val hour : String = if(hoursLong < 10) "0$hoursLong" else hoursLong.toString()
        val minutes : String = if(minutesLong < 10) "0$minutesLong" else minutesLong.toString()

        CoroutineScope(Dispatchers.IO).launch {
            if ("android.intent.action.BOOT_COMPLETED" == intent?.action
                || "android.intent.action.LOCKED_BOOT_COMPLETED" == intent?.action
                || "android.intent.action.QUICKBOOT_POWERON" == intent?.action
                || "com.htc.intent.action.QUICKBOOT_POWERON" == intent?.action) {
                var count = 1;
                // reserve all times
                repeat(24) {
                    if (count == 24) count = 0
                    var localDateString = if (count < 10)
                        TimesUtils.getLocalDateStringFromCalendar(calendar, "0"+count+":00")
                    else TimesUtils.getLocalDateStringFromCalendar(calendar, "${count}:00")

                    AlarmUtils.setRepeatedAlarm(
                        context!!,
                        Constants.RESERVE_ALL_TIMES,
                        1000+count,
                        localDateString
                    )

                    Log.d("TAG", "getCurrentCityName: Alarm reservation at : "+ localDateString)

                    count += 1
                }



                val localDateString = TimesUtils.getLocalDateStringFromCalendar(calendar, "${hour}:${minutes}")
                AlarmUtils.setRepeatedAlarm(context!!, Constants.RESERVE_ALL_TIMES, 1001, localDateString)

            }

        }

    }
}