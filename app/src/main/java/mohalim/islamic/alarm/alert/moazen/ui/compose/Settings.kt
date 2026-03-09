package mohalim.islamic.alarm.alert.moazen.ui.compose

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
        onDismissRequest = { viewModel.setShowPreAzanPerformerSheet(false) },
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            Text(
                text = stringResource(R.string.before_pray_notification_1),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn {
                item {
                    AzanPerformerItem(
                        context, viewModel, dataStore, azanType,
                        stringResource(R.string.before_pray_notification_1), R.raw.pre_salah_1
                    )
                }
                item {
                    AzanPerformerItem(
                        context, viewModel, dataStore, azanType,
                        stringResource(R.string.before_pray_notification_2), R.raw.pre_salah_2
                    )
                }
                item {
                    AzanPerformerItem(
                        context, viewModel, dataStore, azanType,
                        stringResource(R.string.before_pray_notification_3), R.raw.pre_salah_3
                    )
                }
            }
        }
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
        onDismissRequest = { viewModel.setShowAzanPerformerSheet(false) },
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            Text(
                text = stringResource(R.string.prayer_times),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn {
                val performers = listOf(
                    R.string.hamdoon_hamady to R.raw.hamdoon_hamady,
                    R.string.abdulrahman_mossad to R.raw.abdulrahman_mossad,
                    R.string.elharam_elmekky to R.raw.elharam_elmekky,
                    R.string.hafiz_ahmed to R.raw.hafiz_ahmed,
                    R.string.idris_aslami to R.raw.idris_aslami,
                    R.string.islam_sobhy to R.raw.islam_sobhy,
                    R.string.naser_elkatamy to R.raw.naser_elkatamy
                )

                items(performers.size) { index ->
                    val (nameRes, rawRes) = performers[index]
                    AzanPerformerItem(
                        context, viewModel, dataStore, azanType,
                        stringResource(nameRes), rawRes
                    )
                }
            }
        }
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
    var isPlayPressed by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = { Text(performerName) },
        modifier = Modifier.clickable {
            CoroutineScope(Dispatchers.IO).launch {
                when (azanType) {
                    Constants.AZAN_TYPE_PRE_FAGR -> PreferencesUtils.setDefaultPreAzanTypeFagr(dataStore, rawFile)
                    Constants.AZAN_TYPE_FAGR -> PreferencesUtils.setDefaultAzanTypeFagr(dataStore, rawFile)
                    Constants.AZAN_TYPE_PRE_ZOHR -> PreferencesUtils.setDefaultPreAzanTypeDuhur(dataStore, rawFile)
                    Constants.AZAN_TYPE_ZOHR -> PreferencesUtils.setDefaultAzanTypeDuhur(dataStore, rawFile)
                    Constants.AZAN_TYPE_PRE_ASR -> PreferencesUtils.setDefaultPreAzanTypeAsr(dataStore, rawFile)
                    Constants.AZAN_TYPE_ASR -> PreferencesUtils.setDefaultAzanTypeAsr(dataStore, rawFile)
                    Constants.AZAN_TYPE_PRE_MAGHREB -> PreferencesUtils.setDefaultPreAzanTypeMaghrib(dataStore, rawFile)
                    Constants.AZAN_TYPE_MAGHREB -> PreferencesUtils.setDefaultAzanTypeMaghrib(dataStore, rawFile)
                    Constants.AZAN_TYPE_PRE_ESHA -> PreferencesUtils.setDefaultPreAzanTypeIshaa(dataStore, rawFile)
                    Constants.AZAN_TYPE_ESHA -> PreferencesUtils.setDefaultAzanTypeIshaa(dataStore, rawFile)
                }
                withContext(Dispatchers.Main) {
                    viewModel.setShowPreAzanPerformerSheet(false)
                    viewModel.setShowAzanPerformerSheet(false)
                }
            }
        },
        trailingContent = {
            IconButton(onClick = {
                val playerIntent = Intent(context, AzanMediaPlayerService::class.java).apply {
                    putExtra("Media", rawFile)
                    putExtra("AZAN_TYPE", if (isPlayPressed) Constants.AZAN_TYPE_STOP_SOUND else Constants.AZAN_TYPE_PLAY_SOUND)
                }
                context.startForegroundService(playerIntent)
                isPlayPressed = !isPlayPressed
            }) {
                Icon(
                    imageVector = if (isPlayPressed) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = if (isPlayPressed) "Stop" else "Play"
                )
            }
        }
    )
}


@Composable
fun SummerTime(context: Context, dataStore: DataStore<Preferences>, summerTimeState: Boolean) {
    ListItem(
        headlineContent = { Text(stringResource(R.string.summer_time)) },
        supportingContent = { Text(stringResource(R.string.when_summer_time_is_on_prayer_times_will_add_60_minutes_for_every_pray_notification)) },
        trailingContent = {
            Switch(
                checked = summerTimeState,
                onCheckedChange = { checked ->
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            PreferencesUtils.setSummerTime(dataStore, checked)
                            val currentCity = PreferencesUtils.getCurrentCityName(dataStore)
                            AlarmUtils.setAlarms(context, currentCity, dataStore)
                        }
                    }
                }
            )
        },
        modifier = Modifier.clickable {
            runBlocking {
                withContext(Dispatchers.IO) {
                    PreferencesUtils.setSummerTime(dataStore, !summerTimeState)
                    val currentCity = PreferencesUtils.getCurrentCityName(dataStore)
                    AlarmUtils.setAlarms(context, currentCity, dataStore)
                }
            }
        }
    )
}
