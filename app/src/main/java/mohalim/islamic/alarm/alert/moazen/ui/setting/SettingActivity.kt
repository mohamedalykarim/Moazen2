package mohalim.islamic.alarm.alert.moazen.ui.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingUI(this, viewModel, dataStore)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.observeSummerTime()

        runBlocking {
            withContext(Dispatchers.IO) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingUI(context: Context, viewModel: SettingViewModel, dataStore: DataStore<Preferences>) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
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
    val language = Locale.getDefault().language

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.setting),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { (context as? SettingActivity)?.onBackPressedDispatcher?.onBackPressed() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // --- Section: Location & Time ---
            item { SettingSectionHeader(title = stringResource(R.string.current_city)) }
            item {
                val city = Utils.getCurrentCity(context, currentCity)
                val cityName = if (language == "ar") city.arName else city.enName
                SettingListItem(
                    title = stringResource(R.string.current_city),
                    subtitle = cityName,
                    icon = Icons.Default.LocationOn,
                    onClick = { viewModel.setShowCityBottomSheet(true) }
                )
            }
            item { SummerTime(context, dataStore, summerTimeState) }

            // --- Section: Prayer Sound (Azan) ---
            item { SettingSectionHeader(title = stringResource(R.string.prayer_times)) }
            item {
                AzanSettingItem(
                    label = stringResource(R.string.fajr),
                    performer = SettingUtils.getAzanPerformerNameByRawId(context, azanPerformerFagr),
                    onClick = {
                        azanType = Constants.AZAN_TYPE_FAGR
                        viewModel.setShowAzanPerformerSheet(true)
                    }
                )
            }
            item {
                AzanSettingItem(
                    label = stringResource(R.string.duhur),
                    performer = SettingUtils.getAzanPerformerNameByRawId(context, azanPerformerDuhur),
                    onClick = {
                        azanType = Constants.AZAN_TYPE_ZOHR
                        viewModel.setShowAzanPerformerSheet(true)
                    }
                )
            }
            item {
                AzanSettingItem(
                    label = stringResource(R.string.asr),
                    performer = SettingUtils.getAzanPerformerNameByRawId(context, azanPerformerAsr),
                    onClick = {
                        azanType = Constants.AZAN_TYPE_ASR
                        viewModel.setShowAzanPerformerSheet(true)
                    }
                )
            }
            item {
                AzanSettingItem(
                    label = stringResource(R.string.maghrib),
                    performer = SettingUtils.getAzanPerformerNameByRawId(context, azanPerformerMaghrib),
                    onClick = {
                        azanType = Constants.AZAN_TYPE_MAGHREB
                        viewModel.setShowAzanPerformerSheet(true)
                    }
                )
            }
            item {
                AzanSettingItem(
                    label = stringResource(R.string.isha),
                    performer = SettingUtils.getAzanPerformerNameByRawId(context, azanPerformerIshaa),
                    onClick = {
                        azanType = Constants.AZAN_TYPE_ESHA
                        viewModel.setShowAzanPerformerSheet(true)
                    }
                )
            }

            // --- Section: Pre-Azan Notifications ---
            item { SettingSectionHeader(title = stringResource(R.string.before_pray_notification_1)) }
            item {
                AzanSettingItem(
                    label = stringResource(R.string.fajr),
                    performer = SettingUtils.getPreAzanPerformerNameByRawId(context, preAzanPerformerFagr),
                    icon = Icons.Default.NotificationsActive,
                    onClick = {
                        azanType = Constants.AZAN_TYPE_PRE_FAGR
                        viewModel.setShowPreAzanPerformerSheet(true)
                    }
                )
            }
            item {
                AzanSettingItem(
                    label = stringResource(R.string.duhur),
                    performer = SettingUtils.getPreAzanPerformerNameByRawId(context, preAzanPerformerDuhur),
                    icon = Icons.Default.NotificationsActive,
                    onClick = {
                        azanType = Constants.AZAN_TYPE_PRE_ZOHR
                        viewModel.setShowPreAzanPerformerSheet(true)
                    }
                )
            }
            item {
                AzanSettingItem(
                    label = stringResource(R.string.asr),
                    performer = SettingUtils.getPreAzanPerformerNameByRawId(context, preAzanPerformerAsr),
                    icon = Icons.Default.NotificationsActive,
                    onClick = {
                        azanType = Constants.AZAN_TYPE_PRE_ASR
                        viewModel.setShowPreAzanPerformerSheet(true)
                    }
                )
            }
            item {
                AzanSettingItem(
                    label = stringResource(R.string.maghrib),
                    performer = SettingUtils.getPreAzanPerformerNameByRawId(context, preAzanPerformerMaghrib),
                    icon = Icons.Default.NotificationsActive,
                    onClick = {
                        azanType = Constants.AZAN_TYPE_PRE_MAGHREB
                        viewModel.setShowPreAzanPerformerSheet(true)
                    }
                )
            }
            item {
                AzanSettingItem(
                    label = stringResource(R.string.isha),
                    performer = SettingUtils.getPreAzanPerformerNameByRawId(context, preAzanPerformerIshaa),
                    icon = Icons.Default.NotificationsActive,
                    onClick = {
                        azanType = Constants.AZAN_TYPE_PRE_ESHA
                        viewModel.setShowPreAzanPerformerSheet(true)
                    }
                )
            }

            // --- Section: Permissions & Info ---
            item { SettingSectionHeader(title = stringResource(R.string.required_permissions_for_the_application)) }
            item {
                SettingListItem(
                    title = stringResource(R.string.permissions),
                    subtitle = stringResource(R.string.required_permissions_for_the_application),
                    icon = Icons.Default.Security,
                    onClick = { context.startActivity(Intent(context, FirstStartActivity::class.java)) }
                )
            }
        }
    }

    if (showCityBottomSheet) {
        ChooseCityBottomUISettingActivity(context, dataStore, currentCity, language, viewModel)
    }

    if (showAzanPerformerBottomSheet) {
        ChooseAzanPerformerUI(context, viewModel, dataStore, azanType)
    }

    if (showPreAzanPerformerBottomSheet) {
        ChoosePreAzanPerformerUI(context, viewModel, dataStore, azanType)
    }
}

@Composable
fun SettingSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingListItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun AzanSettingItem(
    label: String,
    performer: String,
    icon: ImageVector = Icons.Default.MusicNote,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(label) },
        supportingContent = { Text(performer) },
        leadingContent = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
        modifier = Modifier.clickable { onClick() }
    )
}

/** Legacy support or reusable components if needed **/
@Composable
fun SettingButton(name: String, iconId: Int, onClickCard: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = onClickCard,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = androidx.compose.ui.res.painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = name, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
