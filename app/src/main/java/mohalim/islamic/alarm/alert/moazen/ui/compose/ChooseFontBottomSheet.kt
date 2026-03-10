package mohalim.islamic.alarm.alert.moazen.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseFontBottomSheet(
    viewModel: SettingViewModel,
    dataStore: DataStore<Preferences>
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { viewModel.setShowFontBottomSheet(false) },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.choose_quran_font),
                modifier = Modifier.padding(16.dp)
            )

            FontItem("hafs_smart", stringResource(R.string.font_hafs_smart)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "hafs_smart")
                    viewModel.setShowFontBottomSheet(false)
                }
            }

            FontItem("hafs_18", stringResource(R.string.font_hafs_18)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "hafs_18")
                    viewModel.setShowFontBottomSheet(false)
                }
            }

            FontItem("warsh", stringResource(R.string.font_warsh)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "warsh")
                    viewModel.setShowFontBottomSheet(false)
                }
            }

            FontItem("qaloon", stringResource(R.string.font_qaloon)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "qaloon")
                    viewModel.setShowFontBottomSheet(false)
                }
            }

            FontItem("doori", stringResource(R.string.font_doori)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "doori")
                    viewModel.setShowFontBottomSheet(false)
                }
            }

            FontItem("soosi", stringResource(R.string.font_soosi)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "soosi")
                    viewModel.setShowFontBottomSheet(false)
                }
            }

            FontItem("shouba", stringResource(R.string.font_shouba)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "shouba")
                    viewModel.setShowFontBottomSheet(false)
                }
            }

            FontItem("bazzi", stringResource(R.string.font_bazzi)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "bazzi")
                    viewModel.setShowFontBottomSheet(false)
                }
            }

            FontItem("qumbul", stringResource(R.string.font_qumbul)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "qumbul")
                    viewModel.setShowFontBottomSheet(false)
                }
            }
        }
    }
}

@Composable
fun FontItem(fontValue: String, label: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(label) },
        modifier = Modifier.clickable { onClick() }
    )
}