package mohalim.islamic.alarm.alert.moazen.core.service

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.core.alarm.AlarmUtils
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils

@HiltWorker
class TimerWorker @AssistedInject constructor (@Assisted appContext : Context, @Assisted workerParams: WorkerParameters, val dataStore: DataStore<Preferences>) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        CoroutineScope(Dispatchers.IO).launch {
            PreferencesUtils.getCurrentCityName(dataStore).collect{
                Log.d("TAG", "doWork: ")
                AlarmUtils.setAlarms(applicationContext, it)
            }
        }

        return Result.success()
    }
}