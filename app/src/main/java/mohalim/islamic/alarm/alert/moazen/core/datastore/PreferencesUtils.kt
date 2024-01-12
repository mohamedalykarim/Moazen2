package mohalim.islamic.alarm.alert.moazen.core.datastore

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import mohalim.islamic.alarm.alert.moazen.R
import javax.inject.Inject

class PreferencesUtils @Inject constructor(val context: Context, private val dataStore: DataStore<Preferences>) {


    companion object {

        val IS_FIRST_OPEN = booleanPreferencesKey("is_first_open")
        val CURRENT_CITY_NAME = stringPreferencesKey("current_city_name")

        val IS_FAGR_ALERT_WORK = booleanPreferencesKey("is_fagr_alert_work")
        val IS_DUHUR_ALERT_WORK = booleanPreferencesKey("is_duhur_alert_work")
        val IS_ASR_ALERT_WORK = booleanPreferencesKey("is_asr_alert_work")
        val IS_MAGHRIB_ALERT_WORK = booleanPreferencesKey("is_maghrib_alert_work")
        val IS_ISHAA_ALERT_WORK = booleanPreferencesKey("is_eshaa_alert_work")

        val DEFAULT_AZAN_TYPE_FAGR = intPreferencesKey("default_azan_type_fagr")
        val DEFAULT_AZAN_TYPE_DUHUR = intPreferencesKey("default_azan_type_duhur")
        val DEFAULT_AZAN_TYPE_ASR = intPreferencesKey("default_azan_type_asr")
        val DEFAULT_AZAN_TYPE_MAGHRIB = intPreferencesKey("default_azan_type_maghrib")
        val DEFAULT_AZAN_TYPE_ISHAA = intPreferencesKey("default_azan_type_ishaa")


        suspend fun setIsFirstOpen(dataStore: DataStore<Preferences>, isFirstOpen: Boolean){
            dataStore.edit { settings ->
                settings[IS_FIRST_OPEN] = isFirstOpen
            }
        }
        fun getIsFirstOpen(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_FIRST_OPEN] ?: true
                }
        }



        suspend fun setCurrentCityName(dataStore: DataStore<Preferences>, cityName: String){
            dataStore.edit { settings ->
                settings[CURRENT_CITY_NAME] = cityName
            }
        }

        fun getCurrentCityName(dataStore: DataStore<Preferences>): Flow<String> {
            return dataStore.data
                .map { preferences ->
                    preferences[CURRENT_CITY_NAME] ?: ""
                }
        }

        suspend fun setIsFagrAlertWork(dataStore: DataStore<Preferences>, isWork: Boolean){
            dataStore.edit { settings ->
                settings[IS_FAGR_ALERT_WORK] = isWork
            }
        }

        fun getIsFagrAlertWork(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_FAGR_ALERT_WORK] ?: true
                }
        }

        suspend fun setIsDuhurAlertWork(dataStore: DataStore<Preferences>, isWork: Boolean){
            dataStore.edit { settings ->
                settings[IS_DUHUR_ALERT_WORK] = isWork
                Log.d("TAG", "setIsDuhurAlertWork: "+isWork)
            }
        }

        fun getIsDuhurAlertWork(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_DUHUR_ALERT_WORK] ?: true
                }
        }

        suspend fun setIsAsrAlertWork(dataStore: DataStore<Preferences>, isWork: Boolean){
            dataStore.edit { settings ->
                settings[IS_ASR_ALERT_WORK] = isWork
            }
        }

        fun getIsAsrAlertWork(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_ASR_ALERT_WORK] ?: true
                }
        }

        suspend fun setIsMaghribAlertWork(dataStore: DataStore<Preferences>, isWork: Boolean){
            dataStore.edit { settings ->
                settings[IS_MAGHRIB_ALERT_WORK] = isWork
            }
        }

        fun getIsMaghribAlertWork(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_MAGHRIB_ALERT_WORK] ?: true
                }
        }

        suspend fun setIsIshaaAlertWork(dataStore: DataStore<Preferences>, isWork: Boolean){
            dataStore.edit { settings ->
                settings[IS_ISHAA_ALERT_WORK] = isWork
            }
        }

        fun getIsIshaaAlertWork(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_ISHAA_ALERT_WORK] ?: true
                }
        }

        suspend fun setDefaultAzanTypeFagr(dataStore: DataStore<Preferences>, azanType: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_AZAN_TYPE_FAGR] = azanType
            }
        }

        fun getDefaultAzanTypeFagr(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_AZAN_TYPE_FAGR] ?: R.raw.hamdoon_hamady
                }
        }

        suspend fun setDefaultAzanTypeDuhur(dataStore: DataStore<Preferences>, azanType: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_AZAN_TYPE_DUHUR] = azanType
            }
        }

        fun getDefaultAzanTypeDuhur(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_AZAN_TYPE_DUHUR] ?: R.raw.hamdoon_hamady
                }
        }

        suspend fun setDefaultAzanTypeAsr(dataStore: DataStore<Preferences>, azanType: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_AZAN_TYPE_ASR] = azanType
            }
        }

        fun getDefaultAzanTypeAsr(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_AZAN_TYPE_ASR] ?: R.raw.hamdoon_hamady
                }
        }

        suspend fun setDefaultAzanTypeMaghrib(dataStore: DataStore<Preferences>, azanType: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_AZAN_TYPE_MAGHRIB] = azanType
            }
        }

        fun getDefaultAzanTypeMaghrib(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_AZAN_TYPE_MAGHRIB] ?: R.raw.hamdoon_hamady
                }
        }

        suspend fun setDefaultAzanTypeIshaa(dataStore: DataStore<Preferences>, azanType: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_AZAN_TYPE_ISHAA] = azanType
            }
        }

        fun getDefaultAzanTypeIshaa(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_AZAN_TYPE_ISHAA] ?: R.raw.hamdoon_hamady
                }
        }
    }


}