package mohalim.islamic.alarm.alert.moazen.core.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R
import java.io.IOException


@AndroidEntryPoint
class MediaPlayerService : Service(), MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
    MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, AudioManager.OnAudioFocusChangeListener{


    private var mediaPlayer: MediaPlayer? = null
    var mediaFile_elharam_elmekky: String? = "android.resource://" + packageName + "/" + R.raw.elharam_elmekky
    //Used to pause/resume MediaPlayer
    private var resumePosition : Int = 0

    private lateinit var audioManager: AudioManager;

    private val iBinder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return iBinder
    }

    private fun initMediaPlayer(){
        mediaPlayer = MediaPlayer();
        //Set up MediaPlayer event listeners
        mediaPlayer?.setOnCompletionListener(this);
        mediaPlayer?.setOnErrorListener(this);
        mediaPlayer?.setOnPreparedListener(this);
        mediaPlayer?.setOnBufferingUpdateListener(this);
        mediaPlayer?.setOnSeekCompleteListener(this);
        mediaPlayer?.setOnInfoListener(this);
        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer?.reset();

        val audioAttributes =  AudioAttributes.Builder()
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .setLegacyStreamType(AudioManager.STREAM_ALARM)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        mediaPlayer?.setAudioAttributes(audioAttributes)

        try {
            // Set the data source to the mediaFile location
            mediaPlayer?.setDataSource(mediaFile_elharam_elmekky)
        } catch (e: IOException) {
            e.printStackTrace()
            stopSelf()
        }
        mediaPlayer?.prepareAsync()

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
        TODO("Not yet implemented")
    }

    override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        TODO("Not yet implemented")
    }

    inner class LocalBinder : Binder() {
        fun getService() : MediaPlayerService? {
            return this@MediaPlayerService
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when(focusChange){
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (mediaPlayer == null ) initMediaPlayer()
                else if (!mediaPlayer!!.isPlaying) mediaPlayer?.start()
                mediaPlayer!!.setVolume(1.0f, 1.0f)
            }

            AudioManager.AUDIOFOCUS_LOSS -> {
                if (mediaPlayer!!.isPlaying) mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                if (mediaPlayer!!.isPlaying) mediaPlayer?.pause()
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                if (mediaPlayer!!.isPlaying) mediaPlayer?.setVolume(1.0f, 1.0f)
            }
        }
    }

    private fun requestAudioFocus(): Boolean {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        var result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            return true
        }
        return false
    }

    private fun removeAudioFocus() : Boolean {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            mediaFile_elharam_elmekky = intent?.extras?.getString("media")
        }catch (e : NullPointerException){
            stopSelf()
        }

        if (!requestAudioFocus()){
            stopSelf()
        }

        if (mediaFile_elharam_elmekky != null && mediaFile_elharam_elmekky != "") initMediaPlayer()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null){
            stopMedia()
            mediaPlayer?.release()
        }
        removeAudioFocus()
    }
}