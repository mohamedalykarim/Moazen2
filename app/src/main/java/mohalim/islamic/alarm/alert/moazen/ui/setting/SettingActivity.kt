package mohalim.islamic.alarm.alert.moazen.ui.setting

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.core.utils.SettingUtils
import mohalim.islamic.alarm.alert.moazen.core.utils.Utils
import mohalim.islamic.alarm.alert.moazen.ui.compose.ChooseAzanPerformerUI
import mohalim.islamic.alarm.alert.moazen.ui.compose.ChooseCityBottomUISettingActivity
import mohalim.islamic.alarm.alert.moazen.ui.compose.ChoosePreAzanPerformerUI
import mohalim.islamic.alarm.alert.moazen.ui.compose.SummerTime
import mohalim.islamic.alarm.alert.moazen.ui.main.FirstStartActivity
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

        viewModel.observeSummerTime()
        viewModel.observeSummerTime()


        runBlocking {
            withContext(Dispatchers.IO){
                viewModel.getCurrentCityName()

                viewModel.getAzanPerformerFagr()
                viewModel.getAzanPerformerDuhur()
                viewModel.getAzanPerformerAsr()
                viewModel.getAzanPerformerMaghrib()
                viewModel.getAzanPerformerIshaa()

                viewModel.getPreAzanPerformerFagr()
                viewModel.getPreAzanPerformerDuhur()
                viewModel.getPreAzanPerformerAsr()
                viewModel.getPreAzanPerformerMaghrib()
                viewModel.getPreAzanPerformerIshaa()

            }
        }
    }
}

@Composable
fun SettingUI(context: Context, viewModel: SettingViewModel, dataStore: DataStore<Preferences>) {
    val currentCity by viewModel.currentCity.collectAsState()
    val showCityBottomSheet by viewModel.showCityBottomSheet.collectAsState()
    val showAzanPerformerBottomSheet by viewModel.showAzanPerformerBottomSheet.collectAsState()
    val showPreAzanPerformerBottomSheet by viewModel.showPreAzanPerformerBottomSheet.collectAsState()

    val azanPerformerFagr by viewModel.azanPerformerFagr.collectAsState()
    val azanPerformerDuhur by viewModel.azanPerformerDuhur.collectAsState()
    val azanPerformerAsr by viewModel.azanPerformerAsr.collectAsState()
    val azanPerformerMaghrib by viewModel.azanPerformerMaghrib.collectAsState()
    val azanPerformerIshaa by viewModel.azanPerformerIshaa.collectAsState()

    val preAzanPerformerFagr by viewModel.preAzanPerformerFagr.collectAsState()
    val preAzanPerformerDuhur by viewModel.preAzanPerformerDuhur.collectAsState()
    val preAzanPerformerAsr by viewModel.preAzanPerformerAsr.collectAsState()
    val preAzanPerformerMaghrib by viewModel.preAzanPerformerMaghrib.collectAsState()
    val preAzanPerformerIshaa by viewModel.preAzanPerformerIshaa.collectAsState()

    val summerTimeState by viewModel.summerTimeState.collectAsState()


    var azanType by remember { mutableStateOf("") }

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

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.verticalScroll(scrollState),
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
             * Current City
             */

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(parseColor("#ffe7ee")))
                    .border(
                        1.dp,
                        Color(parseColor("#6c0678")),
                        shape = RoundedCornerShape(7.dp)
                    )
                    .padding(8.dp)

            ) {

                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Current City",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

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

                    SettingButton(name = cityName, iconId = R.drawable.ic_masjed_icon) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({viewModel.setShowCityBottomSheet(true)}, 200)
                    }

                }

            }


            /**
             *  Summer Time
             */
            SummerTime(context, dataStore = dataStore, summerTimeState)

            /**
             * Azan Performer
             */

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(parseColor("#ffe7ee")))
                    .border(
                        1.dp,
                        Color(parseColor("#6c0678")),
                        shape = RoundedCornerShape(7.dp)
                    )
                    .padding(8.dp)

            ) {

                /** Fagr Azan performer **/
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Fagr azan performer",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

                    SettingButton(name = SettingUtils.getAzanPerformerNameByRawId( azanPerformerFagr), iconId = R.drawable.ic_masjed_icon) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                azanType = Constants.AZAN_TYPE_FAGR
                                viewModel.setShowAzanPerformerSheet(true)
                            }, 200)
                    }

                }


                /** Duhur Azan performer **/
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Duhur azan performer",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

                    SettingButton(name = SettingUtils.getAzanPerformerNameByRawId( azanPerformerDuhur), iconId = R.drawable.ic_masjed_icon) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                azanType = Constants.AZAN_TYPE_ZOHR
                                viewModel.setShowAzanPerformerSheet(true)
                            }, 200)
                    }

                }

                /** ASR Azan performer **/
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Asr azan performer",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

                    SettingButton(name = SettingUtils.getAzanPerformerNameByRawId( azanPerformerAsr), iconId = R.drawable.ic_masjed_icon) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                azanType = Constants.AZAN_TYPE_ASR
                                viewModel.setShowAzanPerformerSheet(true)
                            }, 200)
                    }

                }

                /** Maghrib Azan performer **/
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Maghrib azan performer",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

                    SettingButton(name = SettingUtils.getAzanPerformerNameByRawId( azanPerformerMaghrib), iconId = R.drawable.ic_masjed_icon) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                azanType = Constants.AZAN_TYPE_MAGHREB
                                viewModel.setShowAzanPerformerSheet(true)
                            }, 200)
                    }

                }


                /** Ishaa Azan performer **/
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Ishaa azan performer",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

                    SettingButton(name = SettingUtils.getAzanPerformerNameByRawId( azanPerformerIshaa), iconId = R.drawable.ic_masjed_icon) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                azanType = Constants.AZAN_TYPE_ESHA
                                viewModel.setShowAzanPerformerSheet(true)
                            }, 200)
                    }

                }

            }

            /**
             * Before Azan Performer
             */

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(parseColor("#ffe7ee")))
                    .border(
                        1.dp,
                        Color(parseColor("#6c0678")),
                        shape = RoundedCornerShape(7.dp)
                    )
                    .padding(8.dp)

            ) {

                /** Fagr Azan performer **/
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Fagr before azan notification sound",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

                    SettingButton(name = SettingUtils.getPreAzanPerformerNameByRawId(preAzanPerformerFagr), iconId = R.drawable.ic_masjed_icon) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                azanType = Constants.AZAN_TYPE_PRE_FAGR
                                viewModel.setShowPreAzanPerformerSheet(true)
                            }, 200)
                    }

                }


                /** Duhur Azan performer **/
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Duhur before azan notification sound",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

                    SettingButton(name = SettingUtils.getPreAzanPerformerNameByRawId(preAzanPerformerDuhur), iconId = R.drawable.ic_masjed_icon) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                azanType = Constants.AZAN_TYPE_PRE_ZOHR
                                viewModel.setShowPreAzanPerformerSheet(true)
                            }, 200)
                    }

                }

                /** ASR Azan performer **/
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Asr Before azan notification sound",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

                    SettingButton(name = SettingUtils.getPreAzanPerformerNameByRawId(preAzanPerformerAsr), iconId = R.drawable.ic_masjed_icon) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                azanType = Constants.AZAN_TYPE_PRE_ASR
                                viewModel.setShowPreAzanPerformerSheet(true)
                            }, 200)
                    }

                }

                /** Maghrib Azan performer **/
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Maghrib Before azan notification sound",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

                    SettingButton(name = SettingUtils.getPreAzanPerformerNameByRawId(preAzanPerformerMaghrib), iconId = R.drawable.ic_masjed_icon) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                azanType = Constants.AZAN_TYPE_PRE_MAGHREB
                                viewModel.setShowPreAzanPerformerSheet(true)
                            }, 200)
                    }

                }


                /** Ishaa Azan performer **/
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Ishaa Before azan notification sound",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

                    SettingButton(name = SettingUtils.getPreAzanPerformerNameByRawId(preAzanPerformerIshaa), iconId = R.drawable.ic_masjed_icon) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                azanType = Constants.AZAN_TYPE_PRE_ESHA
                                viewModel.setShowPreAzanPerformerSheet(true)
                            }, 200)
                    }

                }

            }


            /**
             * Permission
             */

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(parseColor("#ffe7ee")))
                    .border(
                        1.dp,
                        Color(parseColor("#6c0678")),
                        shape = RoundedCornerShape(7.dp)
                    )
                    .padding(8.dp)

            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "Required permissions for the application",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color(parseColor("#000000"))
                    )

                    SettingButton(name = "Permissions", iconId = R.drawable.ic_masjed_icon, onClickCard = {
                        context.startActivity(Intent(context, FirstStartActivity::class.java))
                    })

                }

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

    if (showAzanPerformerBottomSheet){
        ChooseAzanPerformerUI(context, viewModel = viewModel, dataStore = dataStore, azanType = azanType )
    }

    if (showPreAzanPerformerBottomSheet){
        ChoosePreAzanPerformerUI(context, viewModel = viewModel, dataStore = dataStore, azanType = azanType )
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
            .padding(start = 10.dp, end = 10.dp)
            .height(40.dp)
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
                    .height(40.dp)
                    .padding(4.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                color = Color(parseColor("#ffffff")),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )


        }
    }


}