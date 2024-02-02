package mohalim.islamic.alarm.alert.moazen.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import mohalim.islamic.alarm.alert.moazen.R
import java.util.concurrent.TimeUnit


class WorkManagerService : Service() {

    private val SERVICE_NOTIFICATION_ID = 12345
    override fun onCreate() {
        super.onCreate()

        // Create a notification for the foreground service
        val notification = createNotification()
        startForeground(SERVICE_NOTIFICATION_ID, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        val name = "NotificationChannelName"
        val descriptionText = "NotificationChannelDescription"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel("moazenNotificationChannnel", name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        val contentView = RemoteViews(packageName, R.layout.worker_custom_notification)

        val builder = NotificationCompat.Builder(this, "moazenNotificationChannnel")
            .setSmallIcon(R.drawable.ic_masjed_icon)
            .setColor(Color.parseColor("#66236e"))
            .setColorized(true)
            .setOngoing(true)
            .setCustomContentView(contentView)


        return builder.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start your WorkManager tasks here
        startWorkManagerTasks()

        return START_STICKY
    }

    private fun startWorkManagerTasks() {
        val setAlarmsRequest : PeriodicWorkRequest = PeriodicWorkRequest.Builder(TimerWorker::class.java, 1, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("TimerWorker", ExistingPeriodicWorkPolicy.UPDATE, setAlarmsRequest)

    }
}