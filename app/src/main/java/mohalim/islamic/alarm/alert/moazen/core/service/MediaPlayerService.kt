package mohalim.islamic.alarm.alert.moazen.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R


@AndroidEntryPoint
class MediaPlayerService : Service(), MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
    MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener{

    private val binder = LocalBinder()
    private var mediaPlayer : MediaPlayer? = null
    private var resumePosition : Int = 0



    inner class LocalBinder : Binder(){
        fun getService() : MediaPlayerService = this@MediaPlayerService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val rawId = intent?.getIntExtra("Media", 0)
        val azanType = intent?.getStringExtra("AZAN_TYPE")
        if (rawId == 0) stopSelf()

        initMediaPlayer(rawId!!)

        val filter = IntentFilter().apply {
            addAction("android.media.VOLUME_CHANGED_ACTION")
        }

        // Register receiver for volume press button
        registerReceiver(volumeButtonReceiver, filter)

        notification(azanType)

        return START_STICKY
    }

    private fun notification(azanType: String?) {

        // Create the NotificationChannel.
        val name = "NotificationChannelName"
        val descriptionText = "NotificationChannelDescription"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel("moazenNotificationChannnel", name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        val contentView = RemoteViews(packageName, R.layout.custom_notifications)
        var azanDrawable = R.drawable.remain

        when(azanType){
            "AZAN_TYPE_FAGR"-> azanDrawable = R.drawable.till_fagr
            "AZAN_TYPE_SHROUQ"-> azanDrawable = R.drawable.shrouq
            "AZAN_TYPE_ZOHR"-> azanDrawable = R.drawable.till_zohr
            "AZAN_TYPE_ASR"-> azanDrawable = R.drawable.till_asr
            "AZAN_TYPE_GHROUB"-> azanDrawable = R.drawable.ghroub
            "AZAN_TYPE_MAGHREB"-> azanDrawable = R.drawable.till_maghreb
            "AZAN_TYPE_ESHA"-> azanDrawable = R.drawable.till_eshaa
        }
        contentView.setImageViewResource(R.id.image, azanDrawable)

        val builder = NotificationCompat.Builder(this, "moazenNotificationChannnel")
            .setSmallIcon(R.drawable.ic_masjed_icon)
            .setCustomContentView(contentView)

        val notification = builder.build()

        // Start the service as a foreground service with the notification
        startForeground(1, notification)
    }



    private fun initMediaPlayer(rawId : Int) {
       try {
           mediaPlayer?.reset()
           mediaPlayer?.release()

           mediaPlayer = MediaPlayer()
           mediaPlayer?.setOnCompletionListener(this);
           mediaPlayer?.setOnErrorListener(this);
           mediaPlayer?.setOnPreparedListener(this);
           mediaPlayer?.setOnBufferingUpdateListener(this);
           mediaPlayer?.setOnSeekCompleteListener(this);
           mediaPlayer?.setOnInfoListener(this);

           /** Prepare file **/
           val rawFileDescriptor = resources.openRawResourceFd(R.raw.elharam_elmekky)
           mediaPlayer?.setDataSource(rawFileDescriptor.fileDescriptor, rawFileDescriptor.startOffset, rawFileDescriptor.length)
           rawFileDescriptor.close()


           mediaPlayer?.prepareAsync()
       }catch (exception : Exception){
           Log.d("TAG", "initMediaPlayer: "+exception.message)
       }


    }


    private fun playMedia() {
        if (!mediaPlayer!!.isPlaying) mediaPlayer?.start()
    }

    private fun stopMedia(){
        if (mediaPlayer!!.isPlaying) mediaPlayer?.stop()
    }

    private fun pauseMedia(){
        if (mediaPlayer!!.isPlaying){
            mediaPlayer?.pause()
            resumePosition = mediaPlayer!!.currentPosition
        }
    }

    private fun resumeMedia(){
        if (!mediaPlayer!!.isPlaying){
            mediaPlayer?.seekTo(resumePosition)
            mediaPlayer?.start()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        stopMedia()
        stopSelf()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        playMedia()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        //Invoked when there has been an error during an asynchronous operation
        when (what) {
            MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK -> Log.d(
                "MediaPlayer Error",
                "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK $extra"
            )

            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> Log.d(
                "MediaPlayer Error",
                "MEDIA ERROR SERVER DIED $extra"
            )

            MediaPlayer.MEDIA_ERROR_UNKNOWN -> Log.d(
                "MediaPlayer Error",
                "MEDIA ERROR UNKNOWN $extra"
            )
        }
        return false
    }

    override fun onSeekComplete(mp: MediaPlayer?) {
        Log.d("TAG", "onSeekComplete: ")

    }

    override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.d("TAG", "onInfo: ")
        return true
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        Log.d("TAG", "onBufferingUpdate: ")
    }


    // Create a BroadcastReceiver to handle volume button events
    private val volumeButtonReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == "android.media.VOLUME_CHANGED_ACTION") {
                stopMedia()
                stopSelf()
            }
        }
    }


    override fun onDestroy() {
        unregisterReceiver(volumeButtonReceiver)
        mediaPlayer?.reset()
        mediaPlayer?.release()
        super.onDestroy()
    }


}