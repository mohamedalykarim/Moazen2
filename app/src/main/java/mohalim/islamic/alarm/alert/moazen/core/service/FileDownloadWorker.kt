package mohalim.islamic.alarm.alert.moazen.core.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.repository.NetworkRepository
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.core.utils.HadithUtils

@HiltWorker
class FileDownloadWorker @AssistedInject constructor(
    @Assisted context : Context,
    @Assisted workerParams : WorkerParameters,
    val networkRepository: NetworkRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val url = inputData.getString("URL")
        val fileName = inputData.getString("FILE_NAME")

        return try {
            val rawy = fileName!!.replace(".json", "")
            val name = "Downloading a resource "+ HadithUtils.getRawyName(applicationContext, fileName)
            val descriptionText = "We currently downloading "+HadithUtils.getRawyName(applicationContext, fileName)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel("moazenNotificationChannnel"+rawy, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = applicationContext.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

            val notification = NotificationCompat.Builder(applicationContext, "moazenNotificationChannnel"+rawy)
                .setSmallIcon(R.drawable.ic_masjed_icon)
                .setContentTitle(name)
                .setContentText(descriptionText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(true)
                .setProgress(100,0,false)

            var lastNotificationProgress = 0
            networkRepository.downloadFile(url!!, fileName).collect{downloadProgress->
                if(notificationManager.areNotificationsEnabled()){
                    if(ContextCompat.checkSelfPermission( applicationContext,android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
                        if(downloadProgress.processType == Constants.DOWNLOAD_PROCESS_TYPE_DOWNLOADING){
                            notification.setProgress(100,downloadProgress.percentage, false)
                            notification.setSilent(true)


                            if (downloadProgress.percentage % 2 == 0 && downloadProgress.percentage != lastNotificationProgress){
                                notificationManager.notify(Constants.DOWNLOAD_FILE_NOTIFICATION, notification.build())
                                lastNotificationProgress = downloadProgress.percentage
                            }
                        } else if(downloadProgress.processType == Constants.DOWNLOAD_PROCESS_TYPE_INSTALLING){
                            notification.setProgress(100,downloadProgress.percentage, false)
                            notification.setSilent(true)
                            notification.setContentTitle("Installing a resource "+ HadithUtils.getRawyName(applicationContext, fileName))
                            notification.setContentText("We currently installing " + HadithUtils.getRawyName(applicationContext, fileName))


                            if (downloadProgress.percentage % 2 == 0 && downloadProgress.percentage != lastNotificationProgress){
                                notificationManager.notify(Constants.DOWNLOAD_FILE_NOTIFICATION, notification.build())
                                lastNotificationProgress = downloadProgress.percentage
                            }
                        }
                    }
                }


            }

            notification.setContentText("Download complete")
            notification.setProgress(0, 0, false)
            notification.setOngoing(false)
            notificationManager.notify(Constants.DOWNLOAD_FILE_NOTIFICATION, notification.build())


            Result.success()
        }catch (exception : Exception){
            Result.failure()
        }
    }



}