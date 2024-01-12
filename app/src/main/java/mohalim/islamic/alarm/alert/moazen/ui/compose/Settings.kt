package mohalim.islamic.alarm.alert.moazen.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseAzanPerformerUI(viewModel: SettingViewModel, dataStore: DataStore<Preferences>, azanType: String) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.padding(10.dp),
        onDismissRequest = {
            viewModel.setShowAzanPerformerSheet(false)
        },
        sheetState = sheetState
    ) {
        LazyColumn(content = {
            item{
                AzanPerformerItem(viewModel, dataStore = dataStore, azanType = azanType, performerName = "Hamdoon_hamady", rawFile = R.raw.hamdoon_hamady)
            }

            item{
                AzanPerformerItem(viewModel, dataStore = dataStore, azanType = azanType, performerName = "Abdulrahman Mossad", rawFile = R.raw.abdulrahman_mossad)
            }

            item{
                AzanPerformerItem(viewModel, dataStore = dataStore, azanType = azanType, performerName = "Elharam Elmekky", rawFile = R.raw.elharam_elmekky)
            }

            item{
                AzanPerformerItem(viewModel, dataStore = dataStore, azanType = azanType, performerName = "Hafiz Ahmed", rawFile = R.raw.hafiz_ahmed)
            }

            item{
                AzanPerformerItem(viewModel, dataStore = dataStore, azanType = azanType, performerName = "Idris Aslami", rawFile = R.raw.idris_aslami)
            }

            item{
                AzanPerformerItem(viewModel, dataStore = dataStore, azanType = azanType, performerName = "islam_sobhy", rawFile = R.raw.islam_sobhy)
            }

            item{
                AzanPerformerItem(viewModel, dataStore = dataStore, azanType = azanType, performerName = "Naser Elkatamy", rawFile = R.raw.naser_elkatamy)
            }



            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        })



        Spacer(modifier = Modifier.height(50.dp))

    }
}

@Composable
fun AzanPerformerItem(viewModel: SettingViewModel, dataStore: DataStore<Preferences>, azanType: String, performerName: String, rawFile: Int){
        Column(
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 8.dp)
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
                .padding(5.dp)
        ) {
            Text(
                performerName,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = Color(
                    android.graphics.Color.parseColor("#932f3a")
                )
            )


        }


}