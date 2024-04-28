package mohalim.islamic.alarm.alert.moazen.core.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.TextView
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import kotlin.math.abs


class OverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Inflate the overlay view
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)
        overlayView.setOnTouchListener(object : OnSwipeTouchListener(this@OverlayService){
            override fun onSwipeUp(): Boolean {
                stopService(this@OverlayService)
                return super.onSwipeUp()
            }

            override fun onSwipeDown(): Boolean {
                stopService(this@OverlayService)
                return super.onSwipeDown()
            }

            override fun onSwipeLeft(): Boolean {
                stopService(this@OverlayService)
                return super.onSwipeLeft()
            }

            override fun onSwipeRight(): Boolean {
                stopService(this@OverlayService)
                return super.onSwipeRight()
            }
        })


        // Set the layout parameters for the overlay view
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        Log.d("TAG", "onCreate: from overlay service")

        // Add the overlay view to the window manager
        windowManager.addView(overlayView, params)

        object : CountDownTimer(270000,1000){
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                stopService(this@OverlayService)
            }
        }.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val azanType =intent!!.getStringExtra("AZAN_TYPE")

        val message =
            when(azanType){
                Constants.AZAN_TYPE_PRE_FAGR -> applicationContext.getString(R.string._15_minutes_till_fajr)
                Constants.AZAN_TYPE_FAGR -> applicationContext.getString(R.string.now_fajr_azan)
                Constants.AZAN_TYPE_PRE_ZOHR -> applicationContext.getString(R.string._15_minutes_till_duhur)
                Constants.AZAN_TYPE_ZOHR -> applicationContext.getString(R.string.now_duhur_azan)
                Constants.AZAN_TYPE_PRE_ASR -> applicationContext.getString(R.string._15_minutes_till_asr)
                Constants.AZAN_TYPE_ASR -> applicationContext.getString(R.string.now_asr_azan)
                Constants.AZAN_TYPE_PRE_MAGHREB -> applicationContext.getString(R.string._15_minutes_till_maghreb)
                Constants.AZAN_TYPE_MAGHREB -> applicationContext.getString(R.string.now_maghreb_azan)
                Constants.AZAN_TYPE_PRE_ESHA -> applicationContext.getString(R.string._15_minutes_till_ishaa)
                Constants.AZAN_TYPE_ESHA -> applicationContext.getString(R.string.now_maghreb_ishaa)
                else -> ""
            }

        val overlayText: TextView  = overlayView.findViewById(R.id.overlay_text)
        overlayText.text = message

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayView)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    fun stopService(context : Context){
        val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
        playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_STOP_SOUND)
        context.startService(playerIntent)
        stopSelf()
    }
}

open class OnSwipeTouchListener(val context: Context?) : OnTouchListener {
    companion object {
        private const val SwipeThreshold = 100
        private const val SwipeVelocityThreshold = 100
    }

    private val gestureDetector = GestureDetector(context, GestureListener())

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    open fun onSwipeRight(): Boolean { return false }
    open fun onSwipeLeft(): Boolean { return false }
    open fun onSwipeUp(): Boolean { return false }
    open fun onSwipeDown(): Boolean { return false }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            try {
                val diffY = e2.y - e1!!.y
                val diffX = e2.x - e1.x

                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SwipeThreshold && abs(velocityX) > SwipeVelocityThreshold) {
                        return when {
                            diffX > 0 -> onSwipeRight()
                            else -> onSwipeLeft()
                        }
                    }
                } else if (abs(diffY) > SwipeThreshold && abs(velocityY) > SwipeVelocityThreshold) {
                    return when {
                        diffY > 0 -> onSwipeDown()
                        else -> onSwipeUp()
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return false
        }
    }
}