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

            FontItem("amiri", stringResource(R.string.font_amiri)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "amiri")
                    viewModel.setShowFontBottomSheet(false)
                }
            }

            FontItem("kitab", stringResource(R.string.font_kitab)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "kitab")
                    viewModel.setShowFontBottomSheet(false)
                }
            }

            FontItem("kitab_bold", stringResource(R.string.font_kitab_bold)) {
                scope.launch {
                    PreferencesUtils.setQuranFont(dataStore, "kitab_bold")
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