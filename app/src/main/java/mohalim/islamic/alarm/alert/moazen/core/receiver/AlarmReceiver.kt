package mohalim.islamic.alarm.alert.moazen.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.core.service.AzanMediaPlayerService
import mohalim.islamic.alarm.alert.moazen.core.service.TimerWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var dataStore: DataStore<Preferences>;


    override fun onReceive(context: Context?, intent: Intent?) {
        val azanType = intent?.getStringExtra("AZAN_TYPE") ?: return
        when(azanType){
            "AZAN_TYPE_FAGR" ->{
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsFagrAlertWork(dataStore).collect {
                        if (it){
                            val defaultAzanTypeFagr = PreferencesUtils.getDefaultAzanTypeFagr(dataStore)!!
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultAzanTypeFagr)
                            playerIntent.putExtra("AZAN_TYPE", "AZAN_TYPE_FAGR")
                            context!!.startForegroundService(playerIntent)

                        }
                    }
                }
            }

            "AZAN_TYPE_ZOHR" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsDuhurAlertWork(dataStore).collect {

                        if (it){
                            val defaultAzanTypeDuhur = PreferencesUtils.getDefaultAzanTypeDuhur(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultAzanTypeDuhur)
                            playerIntent.putExtra("AZAN_TYPE", "AZAN_TYPE_ZOHR")
                            context!!.startForegroundService(playerIntent)

                        }
                    }
                }

            }

            "AZAN_TYPE_ASR" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsAsrAlertWork(dataStore).collect {
                        if (it){
                            val defaultAzanTypeAsr = PreferencesUtils.getDefaultAzanTypeAsr(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultAzanTypeAsr)
                            playerIntent.putExtra("AZAN_TYPE", "AZAN_TYPE_ASR")
                            context!!.startForegroundService(playerIntent)
                        }
                    }
                }

            }

            "AZAN_TYPE_MAGHREB" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsMaghribAlertWork(dataStore).collect {
                        if (it){
                            val defaultAzanTypeMaghrib = PreferencesUtils.getDefaultAzanTypeMaghrib(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultAzanTypeMaghrib)
                            playerIntent.putExtra("AZAN_TYPE", "AZAN_TYPE_MAGHREB")
                            context!!.startForegroundService(playerIntent)

                        }
                    }
                }

            }

            "AZAN_TYPE_ESHA" -> {
                Log.d("TAG", "onReceive: AZAN_TYPE_ESHA")
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsIshaaAlertWork(dataStore).collect {
                        if (it){
                            val defaultAzanTypeIshaa = PreferencesUtils.getDefaultAzanTypeIshaa(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultAzanTypeIshaa)
                            playerIntent.putExtra("AZAN_TYPE", "AZAN_TYPE_ESHA")
                            context!!.startForegroundService(playerIntent)
                        }
                    }
                }

            }
        }

        val setAlarmsRequest : PeriodicWorkRequest = PeriodicWorkRequest.Builder(TimerWorker::class.java, 1, TimeUnit.HOURS).build()
        WorkManager.getInstance(context!!).enqueueUniquePeriodicWork("TimerWorker", ExistingPeriodicWorkPolicy.UPDATE, setAlarmsRequest)

    }
}