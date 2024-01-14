package mohalim.islamic.alarm.alert.moazen.ui.compose

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.core.service.AzanMediaPlayerService
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingViewModel


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
                    performerName = "Hamdoon_hamady",
                    rawFile = R.raw.hamdoon_hamady
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = "Abdulrahman Mossad",
                    rawFile = R.raw.abdulrahman_mossad
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = "Elharam Elmekky",
                    rawFile = R.raw.elharam_elmekky
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = "Hafiz Ahmed",
                    rawFile = R.raw.hafiz_ahmed
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = "Idris Aslami",
                    rawFile = R.raw.idris_aslami
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = "islam_sobhy",
                    rawFile = R.raw.islam_sobhy
                )
            }

            item {
                AzanPerformerItem(
                    context,
                    viewModel,
                    dataStore = dataStore,
                    azanType = azanType,
                    performerName = "Naser Elkatamy",
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
                Constants.AZAN_TYPE_FAGR -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultAzanTypeFagr(dataStore, rawFile)
                    }

                }

                Constants.AZAN_TYPE_ZOHR -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultAzanTypeDuhur(dataStore, rawFile)
                    }

                }

                Constants.AZAN_TYPE_ASR -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultAzanTypeAsr(dataStore, rawFile)
                    }

                }

                Constants.AZAN_TYPE_MAGHREB -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultAzanTypeMaghrib(dataStore, rawFile)
                    }

                }

                Constants.AZAN_TYPE_ESHA -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesUtils.setDefaultAzanTypeIshaa(dataStore, rawFile)
                    }

                }
            }

            viewModel.setShowAzanPerformerSheet(false)


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