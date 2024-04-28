package mohalim.islamic.alarm.alert.moazen.ui.azan_popup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants

@AndroidEntryPoint
class AzanPopupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val countDownTimer =  object : CountDownTimer(15000,1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                finish()
            }
        }

        countDownTimer.start()

        val azanType = intent.getStringExtra("AZAN_TYPE")
        setContent {
            AzanPopupActivityUI(azanType)
        }

    }
}

@Composable
fun AzanPopupActivityUI(azanType: String?) {
    val message =
        when(azanType){
            Constants.AZAN_TYPE_PRE_FAGR -> stringResource(R.string._15_minutes_till_fajr)
            Constants.AZAN_TYPE_FAGR -> stringResource(R.string.now_fajr_azan)
            Constants.AZAN_TYPE_PRE_ZOHR -> stringResource(R.string._15_minutes_till_duhur)
            Constants.AZAN_TYPE_ZOHR -> stringResource(R.string.now_duhur_azan)
            Constants.AZAN_TYPE_PRE_ASR -> stringResource(R.string._15_minutes_till_asr)
            Constants.AZAN_TYPE_ASR -> stringResource(R.string.now_asr_azan)
            Constants.AZAN_TYPE_PRE_MAGHREB -> stringResource(R.string._15_minutes_till_maghreb)
            Constants.AZAN_TYPE_MAGHREB -> stringResource(R.string.now_maghreb_azan)
            Constants.AZAN_TYPE_PRE_ESHA -> stringResource(R.string._15_minutes_till_ishaa)
            Constants.AZAN_TYPE_ESHA -> stringResource(R.string.now_maghreb_ishaa)
            else -> ""
        }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(android.graphics.Color.parseColor("#ffffff")))
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.transparent_bg),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                painter = painterResource(id = R.drawable.top),
                contentScale = ContentScale.FillWidth,
                contentDescription = ""
            )

            Column {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = message,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 60.sp,
                    textAlign = TextAlign.Center
                )




            }

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp)
                    .weight(1f, false),
                painter = painterResource(id = R.drawable.bottom),
                contentScale = ContentScale.FillWidth,
                contentDescription = ""
            )
        }
    }
}