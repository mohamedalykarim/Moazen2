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
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var dataStore: DataStore<Preferences>;


    override fun onReceive(context: Context?, intent: Intent?) {
        val azanType = intent?.getStringExtra("AZAN_TYPE") ?: return
        when(azanType){
            Constants.AZAN_TYPE_PRE_FAGR ->{
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsFagrAlertWork(dataStore).collect {
                        if (it){
                            val defaultPreAzanTypeFagr = PreferencesUtils.getDefaultPreAzanTypeFagr(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultPreAzanTypeFagr)
                            playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_PRE_FAGR)
                            context!!.startForegroundService(playerIntent)

                        }
                    }
                }
            }

            Constants.AZAN_TYPE_FAGR ->{
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsFagrAlertWork(dataStore).collect {
                        if (it){
                            val defaultAzanTypeFagr = PreferencesUtils.getDefaultAzanTypeFagr(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultAzanTypeFagr)
                            playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_FAGR)
                            context!!.startForegroundService(playerIntent)
                        }
                    }
                }
            }

            Constants.AZAN_TYPE_PRE_ZOHR -> {
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsDuhurAlertWork(dataStore).collect {

                        if (it){
                            val defaultPreAzanTypeDuhur = PreferencesUtils.getDefaultPreAzanTypeDuhur(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultPreAzanTypeDuhur)
                            playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_PRE_ZOHR)
                            context!!.startForegroundService(playerIntent)

                        }
                    }
                }

            }

            Constants.AZAN_TYPE_ZOHR -> {
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsDuhurAlertWork(dataStore).collect {

                        if (it){
                            val defaultAzanTypeDuhur = PreferencesUtils.getDefaultAzanTypeDuhur(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultAzanTypeDuhur)
                            playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_ZOHR)
                            context!!.startForegroundService(playerIntent)
                        }
                    }
                }

            }

            Constants.AZAN_TYPE_PRE_ASR -> {
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsAsrAlertWork(dataStore).collect {
                        if (it){
                            val defaultPreAzanTypeAsr = PreferencesUtils.getDefaultPreAzanTypeAsr(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultPreAzanTypeAsr)
                            playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_PRE_ASR)
                            context!!.startForegroundService(playerIntent)
                        }
                    }
                }

            }

            Constants.AZAN_TYPE_ASR -> {
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsAsrAlertWork(dataStore).collect {
                        if (it){
                            val defaultAzanTypeAsr = PreferencesUtils.getDefaultAzanTypeAsr(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultAzanTypeAsr)
                            playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_ASR)
                            context!!.startForegroundService(playerIntent)
                        }
                    }
                }

            }


            Constants.AZAN_TYPE_PRE_MAGHREB -> {
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsMaghribAlertWork(dataStore).collect {
                        if (it){
                            val defaultPreAzanTypeMaghrib = PreferencesUtils.getDefaultPreAzanTypeMaghrib(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultPreAzanTypeMaghrib)
                            playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_PRE_MAGHREB)
                            context!!.startForegroundService(playerIntent)

                        }
                    }
                }

            }

            Constants.AZAN_TYPE_MAGHREB -> {
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsMaghribAlertWork(dataStore).collect {
                        if (it){
                            val defaultAzanTypeMaghrib = PreferencesUtils.getDefaultAzanTypeMaghrib(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultAzanTypeMaghrib)
                            playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_MAGHREB)
                            context!!.startForegroundService(playerIntent)

                        }
                    }
                }

            }


            Constants.AZAN_TYPE_PRE_ESHA -> {
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsIshaaAlertWork(dataStore).collect {
                        if (it){
                            val defaultPreAzanTypeIshaa = PreferencesUtils.getDefaultPreAzanTypeIshaa(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultPreAzanTypeIshaa)
                            playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_PRE_ESHA)
                            context!!.startForegroundService(playerIntent)
                        }
                    }
                }

            }

            Constants.AZAN_TYPE_ESHA -> {
                Log.d("TAG", "onReceive: AZAN_TYPE_ESHA")
                CoroutineScope(Dispatchers.IO).launch {
                    PreferencesUtils.getIsIshaaAlertWork(dataStore).collect {
                        if (it){
                            val defaultAzanTypeIshaa = PreferencesUtils.getDefaultAzanTypeIshaa(dataStore)
                            val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                            playerIntent.putExtra("Media", defaultAzanTypeIshaa)
                            playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_ESHA)
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