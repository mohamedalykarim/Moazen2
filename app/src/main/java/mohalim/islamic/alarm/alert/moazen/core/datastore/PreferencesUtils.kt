package mohalim.islamic.alarm.alert.moazen.core.datastore

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class PreferencesUtils @Inject constructor(val context: Context, private val dataStore: DataStore<Preferences>) {


    companion object {

        val IS_FIRST_OPEN = booleanPreferencesKey("is_first_open")
        val CURRENT_CITY_NAME = stringPreferencesKey("current_city_name")


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
    }


}