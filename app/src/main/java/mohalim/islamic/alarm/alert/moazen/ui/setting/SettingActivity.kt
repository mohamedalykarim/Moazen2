package mohalim.islamic.alarm.alert.moazen.ui.setting

import android.content.Context
import android.graphics.Color.parseColor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.utils.Utils
import mohalim.islamic.alarm.alert.moazen.ui.compose.ChooseCityBottomUISettingActivity
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {
    val viewModel: SettingViewModel by viewModels()
    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingUI(this, viewModel, dataStore)
        }
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            withContext(Dispatchers.IO){
                viewModel.getCurrentCityName(this@SettingActivity)
            }
        }
    }
}

@Composable
fun SettingUI(context: Context, viewModel: SettingViewModel, dataStore: DataStore<Preferences>) {
    val currentCity by viewModel.currentCity.collectAsState()
    val showCityBottomSheet by viewModel.showCityBottomSheet.collectAsState()


    val language = Locale.getDefault().language;


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(parseColor("#ffffff")))
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.transparent_bg),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            /** Title **/
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                text = "Settings",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start,
                color = Color(parseColor("#000000"))
            )

            /**
             * Main Setting
             */

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(parseColor("#fff2f6")))
                    .border(
                        1.dp,
                        Color(parseColor("#ffd4e2")),
                        shape = RoundedCornerShape(5)
                    )
                    .padding(8.dp)

            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Azan settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    color = Color(parseColor("#000000"))
                )

                /**
                 * Current City
                 */

                Row {
                    Column(modifier = Modifier.height(50.dp).weight(1f), verticalArrangement = Arrangement.Center) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .height(50.dp)
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            text = "Current City",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            color = Color(parseColor("#000000"))
                        )
                    }
                    val city = Utils.getCurrentCity(context, currentCity)
                    var cityName: String
                    cityName = when (language) {
                        "en" -> {
                            city.enName
                        }

                        "ar" -> {
                            city.arName
                        }

                        else -> {
                            city.enName
                        }
                    }

                    Column(modifier = Modifier.height(50.dp).weight(1f), verticalArrangement = Arrangement.Center) {
                        SettingButton(name = cityName, iconId = R.drawable.ic_masjed_icon) {
                            Handler(Looper.getMainLooper())
                                .postDelayed({viewModel.setShowCityBottomSheet(true)}, 200)
                        }
                    }

                }


                /** Line after city  **/

                Spacer(modifier = Modifier.height(8.dp))


                Column(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(
                            Color(
                                parseColor("#dadada")
                            )
                        )
                ) {}




            }

        }


    }

    if (showCityBottomSheet) {
        ChooseCityBottomUISettingActivity(
            context = context,
            dataStore = dataStore,
            currentCity = currentCity,
            language = language,
            viewModel = viewModel
        )
    }
}

@Composable
fun SettingButton(name: String, iconId : Int, onClickCard : ()-> Unit) {
    var isPressed by remember {
        mutableStateOf(false)
    }
    /** Setting Button **/
    val interactionSetting = remember { MutableInteractionSource() }
    LaunchedEffect(interactionSetting){
        interactionSetting.interactions.collect{interaction->
            when(interaction){
                is PressInteraction.Press ->{
                    isPressed = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        isPressed = false

                    },90)
                }
            }

        }
    }
    val settingScale by animateFloatAsState(
        targetValue =  if (isPressed) 0.5f else 1f,
        animationSpec = tween(durationMillis = 80, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f)),
        label = "settingScale")

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(50.dp)
            .scale(settingScale)
            .clickable(
                interactionSource = interactionSetting,
                indication = null,
                onClick = onClickCard
            )
            .clip(RoundedCornerShape(7.dp))
            .background(
                if (isPressed) Color(parseColor("#521f58")) else Color(
                    parseColor("#66236e")
                )
            )
            .border(
                1.dp,
                if (isPressed) Color(parseColor("#ffffff")) else Color(
                    parseColor("#4e1e54")
                ),
                shape = RoundedCornerShape(7.dp)
            )

    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.transparent_bg),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = name,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(top = 16.dp, start = 8.dp, end = 16.dp)
                    .size(32.dp)
            )
            Text(
                text = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                color = Color(parseColor("#ffffff")),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )


        }
    }


}