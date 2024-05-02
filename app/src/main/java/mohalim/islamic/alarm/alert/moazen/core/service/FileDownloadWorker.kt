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
            val name = "Downloading a resource "+ fileName
            val descriptionText = "We currently downloading $fileName"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel("moazenNotificationChannnel"+fileName, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = applicationContext.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

            val notification = NotificationCompat.Builder(applicationContext, "moazenNotificationChannnel"+fileName)
                .setSmallIcon(R.drawable.ic_masjed_icon)
                .setContentTitle(name)
                .setContentText(descriptionText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(true)
                .setProgress(100,0,false)

            var lastNotificationProgress = 0
            networkRepository.downloadFile(url!!, fileName!!).collect{downloadProgress->
                if(notificationManager.areNotificationsEnabled()){
                    if(ContextCompat.checkSelfPermission( applicationContext,android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
                        notification.setProgress(100,downloadProgress.percentage, false)
                        notification.setSilent(true)


                        if (downloadProgress.percentage % 2 == 0 && downloadProgress.percentage != lastNotificationProgress){
                            notificationManager.notify(Constants.DOWNLOAD_FILE_NOTIFICATION, notification.build())
                            lastNotificationProgress = downloadProgress.percentage
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