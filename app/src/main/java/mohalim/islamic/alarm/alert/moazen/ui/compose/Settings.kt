package mohalim.islamic.alarm.alert.moazen.ui.compose

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.alarm.AlarmUtils
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.core.service.AzanMediaPlayerService
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.core.utils.Utils
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingButton
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePreAzanPerformerUI(
    context: Context,
    viewModel: SettingViewModel,
    dataStore: DataStore<Preferences>,
    azanType: String
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.padding(10.dp), onDismissRequest = {
            viewModel.setShowPreAzanPerformerSheet(false)
        }, sheetState = sheetState
    ) {
        LazyColumn(content = {
            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = stringResource(R.string.before_pray_notification_1),
                    rawFile = R.raw.pre_salah_1
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = stringResource(R.string.before_pray_notification_2),
                    rawFile = R.raw.pre_salah_2
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = stringResource(R.string.before_pray_notification_3),
                    rawFile = R.raw.pre_salah_3
                )
            }


            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        })



        Spacer(modifier = Modifier.height(50.dp))

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseAzanPerformerUI(
    context: Context,
    viewModel: SettingViewModel,
    dataStore: DataStore<Preferences>,
    azanType: String
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.padding(10.dp), onDismissRequest = {
            viewModel.setShowAzanPerformerSheet(false)
        }, sheetState = sheetState
    ) {
        LazyColumn(content = {
            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = stringResource(R.string.hamdoon_hamady),
                    rawFile = R.raw.hamdoon_hamady
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = stringResource(R.string.abdulrahman_mossad),
                    rawFile = R.raw.abdulrahman_mossad
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = stringResource(R.string.elharam_elmekky),
                    rawFile = R.raw.elharam_elmekky
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = stringResource(R.string.hafiz_ahmed),
                    rawFile = R.raw.hafiz_ahmed
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = stringResource(R.string.idris_aslami),
                    rawFile = R.raw.idris_aslami
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = stringResource(R.string.islam_sobhy),
                    rawFile = R.raw.islam_sobhy
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = stringResource(R.string.naser_elkatamy),
                    rawFile = R.raw.naser_elkatamy
                )
            }



            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        })



        Spacer(modifier = Modifier.height(50.dp))

    }
}

@Composable
fun AzanPerformerItem(
    context: Context,
    viewModel: SettingViewModel,
    dataStore: DataStore<Preferences>,
    azanType: String,
    performerName: String,
    rawFile: Int
) {
    Row(modifier = Modifier
        .padding(8.dp, 0.dp, 8.dp, 8.dp)
        .height(40.dp)
        .clickable {
            when (azanType) {
                Constants.AZAN_TYPE_PRE_FAGR -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultPreAzanTypeFagr(dataStore, rawFile)
                        viewModel.setShowPreAzanPerformerSheet(false)

                    }

                }

                Constants.AZAN_TYPE_FAGR -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultAzanTypeFagr(dataStore, rawFile)
                        viewModel.setShowAzanPerformerSheet(false)

                    }

                }

                Constants.AZAN_TYPE_PRE_ZOHR -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultPreAzanTypeDuhur(dataStore, rawFile)
                        viewModel.setShowPreAzanPerformerSheet(false)

                    }

                }

                Constants.AZAN_TYPE_ZOHR -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultAzanTypeDuhur(dataStore, rawFile)
                        viewModel.setShowAzanPerformerSheet(false)

                    }

                }

                Constants.AZAN_TYPE_PRE_ASR -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultPreAzanTypeAsr(dataStore, rawFile)
                        viewModel.setShowPreAzanPerformerSheet(false)

                    }

                }

                Constants.AZAN_TYPE_ASR -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultAzanTypeAsr(dataStore, rawFile)
                        viewModel.setShowAzanPerformerSheet(false)

                    }

                }

                Constants.AZAN_TYPE_PRE_MAGHREB -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultPreAzanTypeMaghrib(dataStore, rawFile)
                        viewModel.setShowPreAzanPerformerSheet(false)

                    }
                }

                Constants.AZAN_TYPE_MAGHREB -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultAzanTypeMaghrib(dataStore, rawFile)
                        viewModel.setShowAzanPerformerSheet(false)

                    }

                }

                Constants.AZAN_TYPE_PRE_ESHA -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultPreAzanTypeIshaa(dataStore, rawFile)
                        viewModel.setShowPreAzanPerformerSheet(false)

                    }

                }

                Constants.AZAN_TYPE_ESHA -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultAzanTypeIshaa(dataStore, rawFile)
                        viewModel.setShowAzanPerformerSheet(false)

                    }

                }


            }


        }
        .background(Color(android.graphics.Color.parseColor("#f5ceda")))
        .padding(5.dp)) {

        Text(
            performerName,
            modifier = Modifier
                .fillMaxSize()
                .weight(5f)
                .wrapContentHeight(Alignment.CenterVertically),
            textAlign = TextAlign.Start,
            color = Color(
                android.graphics.Color.parseColor("#932f3a")
            )
        )

        var isPlayPressed by remember { mutableStateOf(false) }

        IconButton(
            onClick = {
                if (isPlayPressed){
                    val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                    playerIntent.putExtra("Media", rawFile)
                    playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_STOP_SOUND)
                    context.startForegroundService(playerIntent)
                    isPlayPressed = !isPlayPressed
                }else{
                    val playerIntent = Intent(context, AzanMediaPlayerService::class.java)
                    playerIntent.putExtra("Media", rawFile)
                    playerIntent.putExtra("AZAN_TYPE", Constants.AZAN_TYPE_PLAY_SOUND)
                    context.startForegroundService(playerIntent)
                    isPlayPressed = !isPlayPressed
                }


            },
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Icon(painter = painterResource(id = if (isPlayPressed) R.drawable.ic_stop_player else R.drawable.ic_play_player), contentDescription = "Play")
        }




    }


}


@Composable
fun SummerTime(context: Context, dataStore: DataStore<Preferences>, summerTimeState : Boolean){

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(android.graphics.Color.parseColor("#ffe7ee")))
            .border(
                1.dp,
                Color(android.graphics.Color.parseColor("#6c0678")),
                shape = RoundedCornerShape(7.dp)
            )
            .padding(8.dp)

    ) {

        Text(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically),
            text = stringResource(R.string.when_summer_time_is_on_prayer_times_will_add_60_minutes_for_every_pray_notification),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            color = Color(android.graphics.Color.parseColor("#000000"))
        )


        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .height(40.dp)

                .clip(RoundedCornerShape(7.dp))
                .background(
                    Color(
                        android.graphics.Color.parseColor("#66236e")
                    )
                )
                .border(
                    1.dp,
                    Color(android.graphics.Color.parseColor("#4e1e54")),
                    shape = RoundedCornerShape(7.dp)
                )

        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.transparent_bg),
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clickable {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            PreferencesUtils.setSummerTime(dataStore, !summerTimeState)
                            val currentCity = PreferencesUtils.getCurrentCityName(dataStore)
                            AlarmUtils.setAlarms(context, currentCity, dataStore)
                        }
                    }
                }
            ) {

                Checkbox(
                    modifier = Modifier.height(40.dp),
                    checked = summerTimeState,
                    onCheckedChange = {
                        runBlocking {
                            withContext(Dispatchers.IO){
                                PreferencesUtils.setSummerTime(dataStore, !summerTimeState)
                                val currentCity = PreferencesUtils.getCurrentCityName(dataStore)
                                AlarmUtils.setAlarms(context, currentCity, dataStore)                            }
                        }
                    },
                    colors = CheckboxDefaults.colors(uncheckedColor = Color.White)
                )

                Text(
                    text = stringResource(R.string.summer_time),
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(4.dp)
                        .wrapContentHeight(Alignment.CenterVertically),
                    color = Color(android.graphics.Color.parseColor("#ffffff")),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                )

            }

        }





    }
}