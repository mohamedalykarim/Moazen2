package mohalim.islamic.alarm.alert.moazen.ui.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mohalim.islamic.alarm.alert.moazen.core.alarm.AlarmUtils
import mohalim.islamic.alarm.alert.moazen.core.datastore.PreferencesUtils
import mohalim.islamic.alarm.alert.moazen.core.utils.Utils
import mohalim.islamic.alarm.alert.moazen.ui.main.MainActivityViewModel
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseCityBottomUIMainActivity(
    context : Context,
    dataStore: DataStore<Preferences>,
    currentCity: String,
    language: String,
    viewModel : MainActivityViewModel
){
    val cities = Utils.getCitiesFromAssets(context)
    val sheetState = rememberModalBottomSheetState()





    /** First Open Bottom Sheet to choose the city */
    ModalBottomSheet(
        modifier = Modifier.padding(10.dp),
        onDismissRequest = {
            viewModel.setShowCityBottomSheet(false)
            if (currentCity == "") {
                CoroutineScope(Dispatchers.IO).launch {

                    /** Set Alarm for first time after choosing city **/
                    AlarmUtils.setAlarmForFirstTime(context, "Luxor")
                    PreferencesUtils.setIsFirstOpen(dataStore, false)
                    PreferencesUtils.setCurrentCityName(dataStore, "Luxor")

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

        /**
         * Search for city
         */

        val textState = remember { mutableStateOf(TextFieldValue("")) }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 8.dp, end = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(android.graphics.Color.parseColor("#f5ceda")),
                unfocusedBorderColor = Color(android.graphics.Color.parseColor("#f5ceda")),
            ),
            placeholder = {
                Text("Search for specific city", fontSize = 12.sp, color = Color(android.graphics.Color.parseColor("#b5b5b5")))
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

                            CoroutineScope(Dispatchers.IO).launch {

                                /** Set Alarm for first time after choosing city **/
                                AlarmUtils.setAlarmForFirstTime(context, city.name)
                                PreferencesUtils.setIsFirstOpen(dataStore, false)
                                PreferencesUtils.setCurrentCityName(
                                    dataStore,
                                    city.name
                                )

                            }

                            viewModel.setShowCityBottomSheet(false)
                        }
                        .background(Color(android.graphics.Color.parseColor("#f5ceda")))
                        .padding(5.dp)) {

                    val cityName =
                        when (language) {
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
                            android.graphics.Color.parseColor("#932f3a")
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseCityBottomUISettingActivity(
    context : Context,
    dataStore: DataStore<Preferences>,
    currentCity: String,
    language: String,
    viewModel : SettingViewModel
){
    val cities = Utils.getCitiesFromAssets(context)
    val sheetState = rememberModalBottomSheetState()

    /** First Open Bottom Sheet to choose the city */
    ModalBottomSheet(
        modifier = Modifier.padding(10.dp),
        onDismissRequest = {
            viewModel.setShowCityBottomSheet(false)
            if (currentCity == "") {
                CoroutineScope(Dispatchers.IO).launch {

                    /** Set Alarm for first time after choosing city **/
                    AlarmUtils.setAlarmForFirstTime(context, "Luxor")
                    PreferencesUtils.setIsFirstOpen(dataStore, false)
                    PreferencesUtils.setCurrentCityName(dataStore, "Luxor")

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

        /**
         * Search for city
         */

        val textState = remember { mutableStateOf(TextFieldValue("")) }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 8.dp, end = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(android.graphics.Color.parseColor("#f5ceda")),
                unfocusedBorderColor = Color(android.graphics.Color.parseColor("#f5ceda")),
            ),
            placeholder = {
                Text("Search for specific city", fontSize = 12.sp, color = Color(android.graphics.Color.parseColor("#b5b5b5")))
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

                            CoroutineScope(Dispatchers.IO).launch {

                                /** Set Alarm for first time after choosing city **/
                                AlarmUtils.setAlarmForFirstTime(context, city.name)
                                PreferencesUtils.setIsFirstOpen(dataStore, false)
                                PreferencesUtils.setCurrentCityName(
                                    dataStore,
                                    city.name
                                )

                            }

                            viewModel.setShowCityBottomSheet(false)
                        }
                        .background(Color(android.graphics.Color.parseColor("#f5ceda")))
                        .padding(5.dp)) {

                    val cityName =
                        when (language) {
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
                            android.graphics.Color.parseColor("#932f3a")
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