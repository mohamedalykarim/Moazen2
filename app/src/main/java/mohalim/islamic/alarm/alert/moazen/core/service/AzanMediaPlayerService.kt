package mohalim.islamic.alarm.alert.moazen.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants

@AndroidEntryPoint
class AzanMediaPlayerService : Service(), MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
    MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private val binder = LocalBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var resumePosition: Int = 0

    private var isFirstVolumeDown = true
    private lateinit var audioManager: AudioManager
    private var focusRequest: AudioFocusRequest? = null

    // Audio focus listener to handle interruptions like calls or notifications
    private val audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                // Focus regained: restore volume and resume if it was playing
                setMediaPlayerVolume(1.0f)
                resumeMedia()
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                // Permanent loss of focus: stop playback
                stopMedia()
                stopSelf()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // Temporary loss of focus (e.g., incoming call): pause playback
                pauseMedia()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                // Temporary loss of focus, but can duck (e.g., notification): lower volume
                setMediaPlayerVolume(0.2f)
            }
        }
    }

    // BroadcastReceiver to handle volume button events to allow user to mute/stop Azan quickly
    private val volumeButtonReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "android.media.VOLUME_CHANGED_ACTION") {
                val stream = intent.extras?.getInt("android.media.EXTRA_VOLUME_STREAM_TYPE") ?: -1

                if (stream == AudioManager.STREAM_MUSIC) {
                    if (isFirstVolumeDown) {
                        setMediaPlayerVolume(0.30f)
                        isFirstVolumeDown = false
                    } else {
                        stopMedia()
                        stopSelf()
                    }
                }
            }
        }
    }

    inner class LocalBinder : Binder() {
        fun getService(): AzanMediaPlayerService = this@AzanMediaPlayerService
    }

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        createNotificationChannel()

        val filter = IntentFilter("android.media.VOLUME_CHANGED_ACTION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(volumeButtonReceiver, filter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(volumeButtonReceiver, filter)
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            val rawId = intent?.getIntExtra("Media", 0) ?: 0
            val azanType = intent?.getStringExtra("AZAN_TYPE")

            // Handle stop commands immediately
            if (azanType == Constants.AZAN_TYPE_STOP || azanType == Constants.AZAN_TYPE_STOP_SOUND) {
                stopMedia()
                stopSelf()
                return START_NOT_STICKY
            }

            if (rawId != 0) {
                initMediaPlayer(rawId)
                
                val notification = if (azanType == Constants.AZAN_TYPE_PLAY_SOUND) {
                    initNotificationForOthers(getString(R.string.masjed_app_is_playing_sound))
                } else {
                    initNotificationForAzan(azanType)
                }
                
                startForeground(1, notification)
            } else {
                stopSelf()
            }

        } catch (e: Exception) {
            Log.e("AzanService", "Error in onStartCommand: ${e.message}")
            stopSelf()
        }
        return START_STICKY
    }

    private fun createNotificationChannel() {
        val name = "Azan Notification"
        val descriptionText = "Notification channel for Azan service"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel("moazenNotificationChannnel", name, importance)
        mChannel.description = descriptionText
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    private fun initNotificationForAzan(azanType: String?): Notification {
        val contentView = RemoteViews(packageName, R.layout.custom_notifications)
        var azanDrawable = R.drawable.remain

        when (azanType) {
            "AZAN_TYPE_FAGR" -> azanDrawable = R.drawable.till_fagr
            "AZAN_TYPE_SHROUQ" -> azanDrawable = R.drawable.shrouq
            "AZAN_TYPE_ZOHR" -> azanDrawable = R.drawable.till_zohr
            "AZAN_TYPE_ASR" -> azanDrawable = R.drawable.till_asr
            "AZAN_TYPE_GHROUB" -> azanDrawable = R.drawable.ghroub
            "AZAN_TYPE_MAGHREB" -> azanDrawable = R.drawable.till_maghreb
            "AZAN_TYPE_ESHA" -> azanDrawable = R.drawable.till_eshaa
        }
        contentView.setImageViewResource(R.id.image, azanDrawable)

        return NotificationCompat.Builder(this, "moazenNotificationChannnel")
            .setSmallIcon(R.drawable.ic_masjed_icon)
            .setCustomContentView(contentView)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()
    }

    private fun initNotificationForOthers(text: String?): Notification {
        val contentView = RemoteViews(packageName, R.layout.custom_notifications_others)
        contentView.setTextViewText(R.id.text, text)

        return NotificationCompat.Builder(this, "moazenNotificationChannnel")
            .setSmallIcon(R.drawable.ic_masjed_icon)
            .setSilent(true)
            .setCustomContentView(contentView)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()
    }

    private fun initMediaPlayer(rawId: Int) {
        try {
            mediaPlayer?.apply {
                if (isPlaying) stop()
                reset()
                release()
            }

            mediaPlayer = MediaPlayer().apply {
                setOnCompletionListener(this@AzanMediaPlayerService)
                setOnErrorListener(this@AzanMediaPlayerService)
                setOnPreparedListener(this@AzanMediaPlayerService)
                setOnBufferingUpdateListener(this@AzanMediaPlayerService)
                setOnSeekCompleteListener(this@AzanMediaPlayerService)
                setOnInfoListener(this@AzanMediaPlayerService)

                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                setAudioAttributes(audioAttributes)

                val rawFileDescriptor = resources.openRawResourceFd(rawId)
                setDataSource(rawFileDescriptor.fileDescriptor, rawFileDescriptor.startOffset, rawFileDescriptor.length)
                rawFileDescriptor.close()
                prepareAsync()
            }
        } catch (exception: Exception) {
            Log.e("AzanService", "initMediaPlayer error: ${exception.message}")
        }
    }

    private fun requestAudioFocus(): Boolean {
        val playbackAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(playbackAttributes)
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(audioFocusChangeListener)
            .build()

        return audioManager.requestAudioFocus(focusRequest!!) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    private fun abandonAudioFocus() {
        focusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
    }

    private fun playMedia() {
        if (requestAudioFocus()) {
            mediaPlayer?.let {
                if (!it.isPlaying) it.start()
            }
        }
    }

    private fun stopMedia() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
        }
        abandonAudioFocus()
    }

    private fun pauseMedia() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                resumePosition = it.currentPosition
            }
        }
    }

    private fun resumeMedia() {
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            if (requestAudioFocus()) {
                mediaPlayer?.seekTo(resumePosition)
                mediaPlayer?.start()
            }
        }
    }

    private fun setMediaPlayerVolume(volume: Float) {
        mediaPlayer?.setVolume(volume, volume)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        stopMedia()
        stopSelf()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        playMedia()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.e("AzanService", "MediaPlayer error: what=$what, extra=$extra")
        stopSelf()
        return false
    }

    override fun onSeekComplete(mp: MediaPlayer?) {}

    override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean = true

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {}

    override fun onDestroy() {
        try {
            unregisterReceiver(volumeButtonReceiver)
        } catch (e: Exception) {
            // Receiver might not be registered
        }

        stopMedia()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}
