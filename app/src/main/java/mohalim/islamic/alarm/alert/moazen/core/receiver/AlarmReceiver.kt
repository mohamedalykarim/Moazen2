package mohalim.islamic.alarm.alert.moazen.core.receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.alarm.AlarmUtils
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.core.service.AzanMediaPlayerService
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import javax.inject.Inject


@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var dataStore: DataStore<Preferences>;


    override fun onReceive(context: Context?, intent: Intent?) {
        val azanType = intent?.getStringExtra("AZAN_TYPE") ?: return

        when(azanType){
            Constants.AZAN_TYPE_PRE_FAGR ->{
                CoroutineScope(Dispatchers.IO).launch {
                    if (PreferencesUtils.getIsFagrAlertWork(dataStore)){
                        val defaultPreAzanTypeFagr = PreferencesUtils.getDefaultPreAzanTypeFagr(dataStore)
                        val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                        playerIntent.putExtra("Media", defaultPreAzanTypeFagr)
                        playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_PRE_FAGR)
                        context!!.startForegroundService(playerIntent)
                    }



                }
            }

            Constants.AZAN_TYPE_FAGR ->{
                CoroutineScope(Dispatchers.IO).launch {
                    if (PreferencesUtils.getIsFagrAlertWork(dataStore)){
                        val defaultAzanTypeFagr = PreferencesUtils.getDefaultAzanTypeFagr(dataStore)
                        val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                        playerIntent.putExtra("Media", defaultAzanTypeFagr)
                        playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_FAGR)
                        context!!.startForegroundService(playerIntent)
                    }
                }
            }

            Constants.AZAN_TYPE_PRE_ZOHR -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if ( PreferencesUtils.getIsDuhurAlertWork(dataStore)){
                        val defaultPreAzanTypeDuhur = PreferencesUtils.getDefaultPreAzanTypeDuhur(dataStore)
                        val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                        playerIntent.putExtra("Media", defaultPreAzanTypeDuhur)
                        playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_PRE_ZOHR)
                        context!!.startForegroundService(playerIntent)

                    }
                }

            }

            Constants.AZAN_TYPE_ZOHR -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (PreferencesUtils.getIsDuhurAlertWork(dataStore)){
                        val defaultAzanTypeDuhur = PreferencesUtils.getDefaultAzanTypeDuhur(dataStore)
                        val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                        playerIntent.putExtra("Media", defaultAzanTypeDuhur)
                        playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_ZOHR)
                        context!!.startForegroundService(playerIntent)
                    }
                }

            }

            Constants.AZAN_TYPE_PRE_ASR -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (PreferencesUtils.getIsAsrAlertWork(dataStore)){
                        val defaultPreAzanTypeAsr = PreferencesUtils.getDefaultPreAzanTypeAsr(dataStore)
                        val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                        playerIntent.putExtra("Media", defaultPreAzanTypeAsr)
                        playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_PRE_ASR)
                        context!!.startForegroundService(playerIntent)
                    }
                }

            }

            Constants.AZAN_TYPE_ASR -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (PreferencesUtils.getIsAsrAlertWork(dataStore)){
                        val defaultAzanTypeAsr = PreferencesUtils.getDefaultAzanTypeAsr(dataStore)
                        val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                        playerIntent.putExtra("Media", defaultAzanTypeAsr)
                        playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_ASR)
                        context!!.startForegroundService(playerIntent)
                    }
                }

            }


            Constants.AZAN_TYPE_PRE_MAGHREB -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (PreferencesUtils.getIsMaghribAlertWork(dataStore)){
                        val defaultPreAzanTypeMaghrib = PreferencesUtils.getDefaultPreAzanTypeMaghrib(dataStore)
                        val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                        playerIntent.putExtra("Media", defaultPreAzanTypeMaghrib)
                        playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_PRE_MAGHREB)
                        context!!.startForegroundService(playerIntent)

                    }
                }

            }

            Constants.AZAN_TYPE_MAGHREB -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (PreferencesUtils.getIsMaghribAlertWork(dataStore)){
                        val defaultAzanTypeMaghrib = PreferencesUtils.getDefaultAzanTypeMaghrib(dataStore)
                        val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                        playerIntent.putExtra("Media", defaultAzanTypeMaghrib)
                        playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_MAGHREB)
                        context!!.startForegroundService(playerIntent)

                    }
                }

            }


            Constants.AZAN_TYPE_PRE_ESHA -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (PreferencesUtils.getIsIshaaAlertWork(dataStore)){
                        val defaultPreAzanTypeIshaa = PreferencesUtils.getDefaultPreAzanTypeIshaa(dataStore)
                        val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                        playerIntent.putExtra("Media", defaultPreAzanTypeIshaa)
                        playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_PRE_ESHA)
                        context!!.startForegroundService(playerIntent)
                    }
                }

            }

            Constants.AZAN_TYPE_ESHA -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (PreferencesUtils.getIsIshaaAlertWork(dataStore)){
                        val defaultAzanTypeIshaa = PreferencesUtils.getDefaultAzanTypeIshaa(dataStore)
                        val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                        playerIntent.putExtra("Media", defaultAzanTypeIshaa)
                        playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_ESHA)
                        context!!.startForegroundService(playerIntent)
                    }
                }

            }

            Constants.ALKAHF_READ_REMINDER->{


                val name = "NotificationChannelName"
                val descriptionText = "NotificationChannelDescription"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel("moazenNotificationChannnel", name, importance)
                mChannel.description = descriptionText
                // Register the channel with the system. You can't change the importance
                // or other notification behaviors after this.
                val notificationManager = context!!.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)

                if(notificationManager.areNotificationsEnabled()){
                    val notification = NotificationCompat.Builder(context, "moazenNotificationChannnel")
                        .setSmallIcon(R.drawable.ic_masjed_icon)
                        .setContentTitle("Suruh AL_KAHF REMINDER")
                        .setContentText("Its Friday Morining, Could you please read Suruh Al-Kahf")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .build()

                    if(ContextCompat.checkSelfPermission( context,android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
                        NotificationManagerCompat.from(context).notify(Constants.ALARM_ID_ALKAHF_REMINDER, notification)
                    }

                }



            }

            Constants.RESERVE_ALL_TIMES ->{
                if (context != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        AlarmUtils.setAlarms(context, PreferencesUtils.getCurrentCityName(dataStore), dataStore)
                    }
                }

            }


        }

    }
}