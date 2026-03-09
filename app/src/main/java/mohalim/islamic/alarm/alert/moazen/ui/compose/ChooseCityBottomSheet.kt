package mohalim.islamic.alarm.alert.moazen.ui.compose

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.alarm.AlarmUtils
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.core.utils.Utils
import mohalim.islamic.alarm.alert.moazen.ui.main.MainActivityViewModel
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseCityBottomUIMainActivity(
    context: Context,
    dataStore: DataStore<Preferences>,
    currentCity: String,
    language: String,
    viewModel: MainActivityViewModel
) {
    val cities = remember { Utils.getCitiesFromAssets(context) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchedText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.setShowCityBottomSheet(false)
            if (currentCity == "") {
                CoroutineScope(Dispatchers.IO).launch {
                    AlarmUtils.setAlarmForFirstTime(context, "Luxor", dataStore)
                    PreferencesUtils.setIsFirstOpen(dataStore, false)
                    PreferencesUtils.setCurrentCityName(dataStore, "Luxor")
                }
            }
        },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        CitySelectionContent(
            cities = cities,
            searchedText = searchedText,
            onSearchChange = { searchedText = it },
            language = language,
            onCitySelected = { city ->
                CoroutineScope(Dispatchers.IO).launch {
                    AlarmUtils.setAlarmForFirstTime(context, city.name, dataStore)
                    PreferencesUtils.setIsFirstOpen(dataStore, false)
                    PreferencesUtils.setCurrentCityName(dataStore, city.name)
                }
                viewModel.setShowCityBottomSheet(false)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseCityBottomUISettingActivity(
    context: Context,
    dataStore: DataStore<Preferences>,
    currentCity: String,
    language: String,
    viewModel: SettingViewModel
) {
    val cities = remember { Utils.getCitiesFromAssets(context) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchedText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = { viewModel.setShowCityBottomSheet(false) },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        CitySelectionContent(
            cities = cities,
            searchedText = searchedText,
            onSearchChange = { searchedText = it },
            language = language,
            onCitySelected = { city ->
                CoroutineScope(Dispatchers.IO).launch {
                    AlarmUtils.setAlarmForFirstTime(context, city.name, dataStore)
                    PreferencesUtils.setIsFirstOpen(dataStore, false)
                    PreferencesUtils.setCurrentCityName(dataStore, city.name)
                }
                viewModel.setShowCityBottomSheet(false)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectionContent(
    cities: List<mohalim.islamic.alarm.alert.moazen.core.model.City>,
    searchedText: String,
    onSearchChange: (String) -> Unit,
    language: String,
    onCitySelected: (mohalim.islamic.alarm.alert.moazen.core.model.City) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.choose_current_city),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        SearchBar(
            query = searchedText,
            onQueryChange = onSearchChange,
            onSearch = {},
            active = false,
            onActiveChange = {},
            placeholder = { Text(stringResource(R.string.search_for_specific_city)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchedText.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) { }

        val filteredItems = remember(searchedText, cities) {
            cities.filter {
                it.enName.contains(searchedText, ignoreCase = true) ||
                        it.arName.contains(searchedText, ignoreCase = true)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredItems, key = { it.name }) { city ->
                CityItem(city = city, language = language, onClick = { onCitySelected(city) })
            }
        }
    }
}

@Composable
fun CityItem(
    city: mohalim.islamic.alarm.alert.moazen.core.model.City,
    language: String,
    onClick: () -> Unit
) {
    val cityName = when (language) {
        "ar" -> "${city.arCountry} - ${city.arName}"
        else -> "${city.country} - ${city.enName}"
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.LocationCity,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = cityName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
