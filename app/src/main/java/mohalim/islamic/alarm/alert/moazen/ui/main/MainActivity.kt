package mohalim.islamic.alarm.alert.moazen.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.alarm.AlarmUtils
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.core.utils.TimesUtils
import mohalim.islamic.alarm.alert.moazen.core.utils.Utils
import mohalim.islamic.alarm.alert.moazen.ui.more.MoreScreenActivity
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainActivityUi(context = this@MainActivity, viewModel, dataStore)
        }
    }

    override fun onResume() {
        super.onResume()

        runBlocking {
            withContext(Dispatchers.IO) {
                viewModel.checkIfFirstOpen(this@MainActivity)
                viewModel.getCurrentCityName(this@MainActivity)
                viewModel.observeIsFagrAlertsWorks()
                viewModel.observeIsDuhurAlertsWorks()
                viewModel.observeIsAsrAlertsWorks()
                viewModel.observeIsMaghribAlertsWorks()
                viewModel.observeIsIshaaAlertsWorks()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.stopCounter()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityUi(
    context: Context,
    viewModel: MainActivityViewModel,
    dataStore: DataStore<Preferences>
) {
    val coroutineScope = rememberCoroutineScope()

    val showCityBottomSheet by viewModel.showCityBottomSheet.collectAsState()
    val showPrayersBottomSheet by viewModel.showPrayersBottomSheet.collectAsState()
    val timer by viewModel.timer.collectAsState()
    val nextPrayType by viewModel.nextPrayerType.collectAsState()
    val currentCity by viewModel.currentCity.collectAsState()
    val prayersForToday by viewModel.prayersForToday.collectAsState()
    val isNextDay by viewModel.isNextDay.collectAsState()
    val midday by viewModel.midday.collectAsState()

    val isFagerAlertWork by viewModel.isFagerAlertWork.collectAsState()
    val isDuhurAlertWork by viewModel.isDuhurAlertWork.collectAsState()
    val isAsrAlertWork by viewModel.isAsrAlertWork.collectAsState()
    val isMaghribAlertWork by viewModel.isMaghribAlertWork.collectAsState()
    val isIshaaAlertWork by viewModel.isIshaaAlertWork.collectAsState()


    val sheetState = rememberModalBottomSheetState()
    val prayersSheetState = rememberModalBottomSheetState()



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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 16.dp, 0.dp, 0.dp),
                    text = "Till Next Prayer:",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )



                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),  //important
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var azanDrawable = R.drawable.remain
                    when (nextPrayType) {
                        "AZAN_TYPE_FAGR" -> azanDrawable = R.drawable.till_fagr
                        "AZAN_TYPE_SHROUQ" -> azanDrawable = R.drawable.shrouq
                        "AZAN_TYPE_ZOHR" -> azanDrawable = R.drawable.till_zohr
                        "AZAN_TYPE_ASR" -> azanDrawable = R.drawable.till_asr
                        "AZAN_TYPE_GHROUB" -> azanDrawable = R.drawable.ghroub
                        "AZAN_TYPE_MAGHREB" -> azanDrawable = R.drawable.till_maghreb
                        "AZAN_TYPE_ESHA" -> azanDrawable = R.drawable.till_eshaa
                    }
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        painter = painterResource(id = azanDrawable),
                        contentDescription = "till Zohr image",
                        contentScale = ContentScale.FillWidth
                    )

                }

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = timer,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 60.sp,
                    textAlign = TextAlign.Center
                )


                Spacer(modifier = Modifier.height(16.dp))


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row {

                        /** Prayers Button */
                        val interactionPrayerTimeSource = remember { MutableInteractionSource() }
                        val isPrayersPressed by interactionPrayerTimeSource.collectIsPressedAsState()
                        val scalePrayers = if (isPrayersPressed) .90f else 1f

                        Column(
                            modifier = Modifier
                                .width(170.dp)
                                .scale(scalePrayers),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(50.dp)
                                    .clickable(
                                        interactionSource = interactionPrayerTimeSource,
                                        indication = null,
                                        onClick = {
                                            viewModel.setShowPrayersBottomSheet(true)
                                        }
                                    )
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isPrayersPressed) Color(parseColor("#ffffff")) else Color(
                                            parseColor("#f5f5f5")
                                        )
                                    )
                                    .border(
                                        1.dp,
                                        if (isPrayersPressed) Color(parseColor("#313131")) else Color(
                                            parseColor("#9c1f2d")
                                        ),
                                        shape = RoundedCornerShape(20)
                                    ),
                            ) {
                                Row {
                                    Image(
                                        modifier = Modifier
                                            .padding(top = 9.dp)
                                            .width(32.dp),
                                        painter = painterResource(id = R.drawable.clock),
                                        contentDescription = "Prayer Times Icon"
                                    )
                                    Text(
                                        modifier = Modifier.padding(top = 13.dp),
                                        text = "Prayer Times",
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color(parseColor("#932f3a"))
                                    )
                                }
                            }
                        }

                        /** More Button */
                        val interactionMore = remember { MutableInteractionSource() }
                        val isMorePressed by interactionMore.collectIsPressedAsState()
                        val scaleMore = if (isMorePressed) .90f else 1.0f


                        Column(
                            modifier = Modifier
                                .width(170.dp)
                                .scale(scaleMore),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(50.dp)
                                    .clickable(
                                        interactionSource = interactionMore,
                                        indication = null,
                                        onClick = {
                                            context.startActivity(
                                                Intent(
                                                    context,
                                                    MoreScreenActivity::class.java
                                                )
                                            )
                                        }
                                    )
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isMorePressed) Color(parseColor("#ffffff")) else Color(
                                            parseColor("#f5f5f5")
                                        )
                                    )
                                    .border(
                                        1.dp,
                                        if (isMorePressed) Color(parseColor("#313131")) else Color(
                                            parseColor("#9c1f2d")
                                        ),
                                        shape = RoundedCornerShape(20)
                                    ),
                            ) {
                                Row {
                                    Image(
                                        modifier = Modifier
                                            .padding(top = 13.dp, start = 7.dp)
                                            .width(22.dp),
                                        painter = painterResource(id = R.drawable.more_icon),
                                        contentDescription = "Prayer Times Icon"
                                    )
                                    Text(
                                        modifier = Modifier.padding(top = 13.dp, start = 6.dp),
                                        text = "More",
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color(parseColor("#932f3a"))
                                    )
                                }
                            }
                        }
                    }
                }

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


        if (showCityBottomSheet) {
            val cities = Utils.getCitiesFromAssets(context)

            /** First Open Bottom Sheet to choose the city */
            ModalBottomSheet(
                modifier = Modifier.padding(10.dp),
                onDismissRequest = {
                    viewModel.setShowCityBottomSheet(false)
                    if (currentCity == "") {
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                /** Set Alarm for first time after choosing city **/
                                AlarmUtils.setAlarmForFirstTime(context, "Luxor")
                                PreferencesUtils.setIsFirstOpen(dataStore, false)
                                PreferencesUtils.setCurrentCityName(dataStore, "Luxor")
                            }
                        }
                    }
                },
                sheetState = sheetState
            ) {

                Text(
                    "Choose Current City", modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 16.dp)
                        .fillMaxWidth(), textAlign = TextAlign.Center
                )

                /**
                 * Search for city
                 */

                val textState = remember { mutableStateOf(TextFieldValue("")) }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(start = 8.dp,  end = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(parseColor("#f5ceda")),
                        unfocusedBorderColor = Color(parseColor("#f5ceda")),
                    ),
                    placeholder = {
                        Text("Search for specific city", fontSize = 12.sp, color = Color(parseColor("#b5b5b5")))
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 12.sp),

                    value = textState.value,
                    onValueChange = { value ->
                        textState.value = value

                    }
                )
                val searchedText = textState.value.text


                Spacer(modifier = Modifier.height(16.dp))


                LazyColumn(content = {

                    val filteredItems =  cities.filter {
                        it.enName.contains(searchedText,ignoreCase = true)
                                || it.arName.contains(searchedText,ignoreCase = true)
                    }

                    items(filteredItems) { city ->

                            Column(
                                modifier = Modifier
                                    .padding(8.dp, 0.dp, 8.dp, 8.dp)
                                    .clickable {
                                        /** set first open false **/
                                        coroutineScope.launch {
                                            withContext(Dispatchers.IO) {
                                                /** Set Alarm for first time after choosing city **/
                                                AlarmUtils.setAlarmForFirstTime(context, city.name)
                                                PreferencesUtils.setIsFirstOpen(dataStore, false)
                                                PreferencesUtils.setCurrentCityName(
                                                    dataStore,
                                                    city.name
                                                )
                                            }
                                        }

                                        viewModel.setShowCityBottomSheet(false)
                                    }
                                    .background(Color(parseColor("#f5ceda")))
                                    .padding(5.dp)) {

                                val cityName =
                                    when (Locale.getDefault().language) {
                                        "en" -> {
                                            "${city.country} - ${city.enName}"
                                        }

                                        "ar" -> {
                                            "${city.arCountry} - ${city.arName}"
                                        }

                                        else -> {
                                            "${city.country} - ${city.enName}"
                                        }
                                    }
                                Text(
                                    cityName,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start,
                                    color = Color(
                                        parseColor("#932f3a")
                                    )
                                )
                            }


                    }
                    
                    item { 
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                })



                Spacer(modifier = Modifier.height(50.dp))
            }


        }
        if (showPrayersBottomSheet) {
            /** Prayer Times Bottom Sheet to show today times **/
            ModalBottomSheet(
                modifier = Modifier.padding(10.dp),
                onDismissRequest = {
                    viewModel.setShowPrayersBottomSheet(false)
                },
                sheetState = prayersSheetState
            ) {
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        val calendar = Calendar.getInstance()
                        var hijrahDate = HijrahDate.now()


                        if (isNextDay) {
                            calendar.timeInMillis = calendar.timeInMillis + 24 * 60 * 60 * 1000
                            hijrahDate = hijrahDate.plus(1, ChronoUnit.DAYS)
                        }

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = TimesUtils.getDate(calendar),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            color = Color(parseColor("#9a212a"))
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = TimesUtils.getHigriDate(hijrahDate),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = Color(parseColor("#313131"))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            modifier = Modifier
                                .background(Color(parseColor("#fff2f6")))
                                .border(
                                    1.dp,
                                    Color(parseColor("#ffd4e2")),
                                    shape = RoundedCornerShape(5)
                                )
                                .padding(16.dp)
                        ) {
                            Row(modifier = Modifier.padding(8.dp)) {
                                Image(
                                    modifier = Modifier.width(32.dp),
                                    painter = painterResource(id = R.drawable.clock),
                                    contentDescription = "Prayer Times"
                                )
                                Column {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Prayer Times",
                                        fontSize = 23.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        textAlign = TextAlign.Start,
                                        color = Color(parseColor("#000000"))
                                    )
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Prayer Times for $currentCity",
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Start,
                                        color = Color(parseColor("#969498"))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))


                            /**
                             * Fagr Time
                             */
                            Row {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp)
                                ) {
                                    Image(
                                        modifier = Modifier.width(32.dp),
                                        painter = painterResource(id = R.drawable.ic_fajr_icon),
                                        contentDescription = "Prayer Times"
                                    )
                                }
                                Text(
                                    modifier = Modifier
                                        .weight(3f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp),
                                    text = "Fajr",
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Color(parseColor("#313131"))
                                )

                                Text(
                                    modifier = Modifier
                                        .weight(2f)
                                        .padding(0.dp, 8.dp, 0.dp, 0.dp),
                                    text = prayersForToday[0],
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Color(parseColor("#313131"))
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp)
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .width(25.dp)
                                            .clickable {
                                                coroutineScope.launch {
                                                    withContext(Dispatchers.IO) {
                                                        viewModel.setIsFagrAlertWork(!isFagerAlertWork)
                                                    }
                                                }
                                            },
                                        painter = painterResource(if (isFagerAlertWork) R.drawable.bell_work else R.drawable.bell_not_work),
                                        contentDescription = "Prayer Times"
                                    )
                                }
                            }

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

                            /**
                             * Duhur Time
                             */

                            Row {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp)
                                ) {
                                    Image(
                                        modifier = Modifier.width(32.dp),
                                        painter = painterResource(id = R.drawable.ic_duhur_icon),
                                        contentDescription = "Prayer Times"
                                    )
                                }
                                Text(
                                    modifier = Modifier
                                        .weight(3f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp),
                                    text = "Duhur",
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Color(parseColor("#313131"))
                                )

                                Text(
                                    modifier = Modifier
                                        .weight(2f)
                                        .padding(0.dp, 8.dp, 0.dp, 0.dp),
                                    text = prayersForToday[2],
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Color(parseColor("#313131"))
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp)
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .width(25.dp)
                                            .clickable {
                                                coroutineScope.launch {
                                                    withContext(Dispatchers.IO) {
                                                        viewModel.setIsDuhurAlertWork(!isDuhurAlertWork)
                                                    }
                                                }
                                            },
                                        painter = painterResource(if (isDuhurAlertWork) R.drawable.bell_work else R.drawable.bell_not_work),
                                        contentDescription = "Prayer Times"
                                    )
                                }
                            }

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

                            /**
                             * ASR Time
                             */

                            Row {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp)
                                ) {
                                    Image(
                                        modifier = Modifier.width(25.dp),
                                        painter = painterResource(id = R.drawable.ic_asr_icon),
                                        contentDescription = "Prayer Times"
                                    )
                                }
                                Text(
                                    modifier = Modifier
                                        .weight(3f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp),
                                    text = "Asr",
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Color(parseColor("#313131"))
                                )

                                Text(
                                    modifier = Modifier
                                        .weight(2f)
                                        .padding(0.dp, 8.dp, 0.dp, 0.dp),
                                    text = prayersForToday[3],
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Color(parseColor("#313131"))
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp)
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .width(25.dp)
                                            .clickable {
                                                coroutineScope.launch {
                                                    withContext(Dispatchers.IO) {
                                                        viewModel.setIsAsrAlertWork(!isAsrAlertWork)
                                                    }
                                                }
                                            },
                                        painter = painterResource(if (isAsrAlertWork) R.drawable.bell_work else R.drawable.bell_not_work),
                                        contentDescription = "Prayer Times"
                                    )
                                }
                            }

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

                            /**
                             * Maghrib Time
                             */

                            Row {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp)
                                ) {
                                    Image(
                                        modifier = Modifier.width(25.dp),
                                        painter = painterResource(id = R.drawable.ic_maghrib_icon),
                                        contentDescription = "Prayer Times"
                                    )
                                }
                                Text(
                                    modifier = Modifier
                                        .weight(3f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp),
                                    text = "Maghrib",
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Color(parseColor("#313131"))
                                )

                                Text(
                                    modifier = Modifier
                                        .weight(2f)
                                        .padding(0.dp, 8.dp, 0.dp, 0.dp),
                                    text = prayersForToday[5],
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Color(parseColor("#313131"))
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp)
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .width(25.dp)
                                            .clickable {
                                                coroutineScope.launch {
                                                    withContext(Dispatchers.IO) {
                                                        viewModel.setIsMaghribAlertWork(!isMaghribAlertWork)
                                                    }
                                                }
                                            },
                                        painter = painterResource(if (isMaghribAlertWork) R.drawable.bell_work else R.drawable.bell_not_work),
                                        contentDescription = "Prayer Times"
                                    )
                                }
                            }

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

                            /**
                             * Isha' Time
                             */

                            Row {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(16.dp, 11.dp, 0.dp, 40.dp)
                                ) {
                                    Image(
                                        modifier = Modifier.width(16.dp),
                                        painter = painterResource(id = R.drawable.ic_isha_icon),
                                        contentDescription = "Prayer Times"
                                    )
                                }
                                Text(
                                    modifier = Modifier
                                        .weight(3f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp),
                                    text = "Isha'",
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Color(parseColor("#313131"))
                                )

                                Text(
                                    modifier = Modifier
                                        .weight(2f)
                                        .padding(0.dp, 8.dp, 0.dp, 0.dp),
                                    text = prayersForToday[6],
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Color(parseColor("#313131"))
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .padding(8.dp, 8.dp, 0.dp, 0.dp)
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .width(25.dp)
                                            .clickable {
                                                coroutineScope.launch {
                                                    withContext(Dispatchers.IO) {
                                                        viewModel.setIsIshaaAlertWork(!isIshaaAlertWork)
                                                    }
                                                }
                                            },
                                        painter = painterResource(if (isIshaaAlertWork) R.drawable.bell_work else R.drawable.bell_not_work),
                                        contentDescription = "Prayer Times"
                                    )
                                }
                            }


                        }

                        Spacer(modifier = Modifier.height(16.dp))


                        Column(
                            modifier = Modifier
                                .background(Color(parseColor("#fff2f6")))
                                .border(
                                    1.dp,
                                    Color(parseColor("#ffd4e2")),
                                    shape = RoundedCornerShape(20)
                                )
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                Column(
                                    Modifier.width(100.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Sunrise",
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color(parseColor("#969498"))
                                    )

                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = prayersForToday[1],
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color(parseColor("#313131"))
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(40.dp)
                                        .background(Color(parseColor("#dadada")))
                                ) {}


                                Column(
                                    Modifier.width(100.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(5.dp))

                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "MidDay",
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color(parseColor("#969498"))
                                    )

                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = midday,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color(parseColor("#313131"))
                                    )

                                }

                                Column(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(40.dp)
                                        .background(Color(parseColor("#dadada")))
                                ) {}


                                Column(
                                    Modifier.width(100.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(5.dp))

                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Sunset",
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color(parseColor("#969498"))
                                    )

                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = prayersForToday[4],
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color(parseColor("#313131"))
                                    )

                                }
                            }
                        }
                    }
                }

            }
        }
    }
}