package mohalim.islamic.alarm.alert.moazen.ui.hadith.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.utils.HadithUtils

class HadithViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rawy = intent.getStringExtra("RAWY_HADITH")

        setContent {
            HadithViewerActivityUI()
        }
    }


}

@Composable
fun HadithViewerActivityUI(){

}