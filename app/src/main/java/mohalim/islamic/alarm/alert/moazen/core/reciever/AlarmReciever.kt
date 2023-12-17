package mohalim.islamic.alarm.alert.moazen.core.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.service.MediaPlayerService


class AlarmReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TAG", "onReceive: AlarmReciever")
        val azanType = intent?.getStringExtra("AZAN_TYPE") ?: return
        val playerIntent = Intent(context, MediaPlayerService::class.java)
        val mediaUri = "android.resource://" + context!!.packageName + "/" + R.raw.elharam_elmekky
        playerIntent.putExtra("media", mediaUri)
        context.startService(playerIntent)
    }
}