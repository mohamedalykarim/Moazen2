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

class PreferencesUtils(val context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

        val IS_FIRST_OPEN = booleanPreferencesKey("is_first_open")

        suspend fun setIsFirstOpen(isFirstOpen: Boolean){
            context.dataStore.edit { settings ->
                settings[IS_FIRST_OPEN] = isFirstOpen
            }
        }

        fun getIsFirstOpen(): Flow<Boolean> {
            return context.dataStore.data
                .map { preferences ->
                    preferences[IS_FIRST_OPEN] ?: true
                }
        }


}