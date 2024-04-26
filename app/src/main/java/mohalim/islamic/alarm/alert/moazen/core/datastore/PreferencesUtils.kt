package mohalim.islamic.alarm.alert.moazen.core.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

        val DEFAULT_PRE_AZAN_TYPE_FAGR = intPreferencesKey("default_pre_azan_type_fagr")
        val DEFAULT_PRE_AZAN_TYPE_DUHUR = intPreferencesKey("default_pre_azan_type_duhur")
        val DEFAULT_PRE_AZAN_TYPE_ASR = intPreferencesKey("default_pre_azan_type_asr")
        val DEFAULT_PRE_AZAN_TYPE_MAGHRIB = intPreferencesKey("default_pre_azan_type_maghrib")
        val DEFAULT_PRE_AZAN_TYPE_ISHAA = intPreferencesKey("default_pre_azan_type_ishaa")

        val DEFAULT_AZAN_TYPE_FAGR = intPreferencesKey("default_azan_type_fagr")
        val DEFAULT_AZAN_TYPE_DUHUR = intPreferencesKey("default_azan_type_duhur")
        val DEFAULT_AZAN_TYPE_ASR = intPreferencesKey("default_azan_type_asr")
        val DEFAULT_AZAN_TYPE_MAGHRIB = intPreferencesKey("default_azan_type_maghrib")
        val DEFAULT_AZAN_TYPE_ISHAA = intPreferencesKey("default_azan_type_ishaa")

        val LAST_VERSION = intPreferencesKey("last_version")


        val SUMMER_TIME = booleanPreferencesKey("summer_time")
        val QURAN_PAGE_REFERENCE = intPreferencesKey("quran_page_reference")


        suspend fun setIsFirstOpen(dataStore: DataStore<Preferences>, isFirstOpen: Boolean){
            dataStore.edit { settings ->
                settings[IS_FIRST_OPEN] = isFirstOpen
            }
        }
        fun observeIsFirstOpen(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_FIRST_OPEN] ?: true
                }
        }

        suspend fun getIsFirstOpen(dataStore: DataStore<Preferences>): Boolean {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_FIRST_OPEN] ?: true
                }.first()
        }



        suspend fun setCurrentCityName(dataStore: DataStore<Preferences>, cityName: String){
            dataStore.edit { settings ->
                settings[CURRENT_CITY_NAME] = cityName
            }
        }

        fun observeCurrentCityName(dataStore: DataStore<Preferences>): Flow<String> {
            return dataStore.data
                .map { preferences ->
                    preferences[CURRENT_CITY_NAME] ?: ""
                }
        }

        suspend fun getCurrentCityName(dataStore: DataStore<Preferences>): String {
            val preferences =  dataStore.data.first()
            return preferences[CURRENT_CITY_NAME] ?: "Luxor"
        }

        suspend fun setIsFagrAlertWork(dataStore: DataStore<Preferences>, isWork: Boolean){
            dataStore.edit { settings ->
                settings[IS_FAGR_ALERT_WORK] = isWork
            }
        }

        fun observeIsFagrAlertWork(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_FAGR_ALERT_WORK] ?: true
                }
        }

        suspend fun getIsFagrAlertWork(dataStore: DataStore<Preferences>): Boolean {
            return dataStore.data.first()[IS_FAGR_ALERT_WORK] ?: true

        }

        suspend fun setIsDuhurAlertWork(dataStore: DataStore<Preferences>, isWork: Boolean){
            dataStore.edit { settings ->
                settings[IS_DUHUR_ALERT_WORK] = isWork
                Log.d("TAG", "setIsDuhurAlertWork: "+isWork)
            }
        }

        fun observeIsDuhurAlertWork(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_DUHUR_ALERT_WORK] ?: true
                }
        }

        suspend fun getIsDuhurAlertWork(dataStore: DataStore<Preferences>): Boolean {
            return dataStore.data.first()[IS_DUHUR_ALERT_WORK] ?: true
        }

        suspend fun setIsAsrAlertWork(dataStore: DataStore<Preferences>, isWork: Boolean){
            dataStore.edit { settings ->
                settings[IS_ASR_ALERT_WORK] = isWork
            }
        }

        fun observeIsAsrAlertWork(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_ASR_ALERT_WORK] ?: true
                }
        }

        suspend fun getIsAsrAlertWork(dataStore: DataStore<Preferences>): Boolean {
            return dataStore.data.first()[IS_ASR_ALERT_WORK] ?: true
        }

        suspend fun setIsMaghribAlertWork(dataStore: DataStore<Preferences>, isWork: Boolean){
            dataStore.edit { settings ->
                settings[IS_MAGHRIB_ALERT_WORK] = isWork
            }
        }

        fun observeIsMaghribAlertWork(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_MAGHRIB_ALERT_WORK] ?: true
                }
        }

        suspend fun getIsMaghribAlertWork(dataStore: DataStore<Preferences>): Boolean {
            return dataStore.data.first()[IS_MAGHRIB_ALERT_WORK] ?: true
        }

        suspend fun setIsIshaaAlertWork(dataStore: DataStore<Preferences>, isWork: Boolean){
            dataStore.edit { settings ->
                settings[IS_ISHAA_ALERT_WORK] = isWork
            }
        }

        fun observeIsIshaaAlertWork(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[IS_ISHAA_ALERT_WORK] ?: true
                }
        }

        suspend fun getIsIshaaAlertWork(dataStore: DataStore<Preferences>): Boolean {
            return dataStore.data.first()[IS_ISHAA_ALERT_WORK] ?: true
        }

        suspend fun setDefaultPreAzanTypeFagr(dataStore: DataStore<Preferences>, resource: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_PRE_AZAN_TYPE_FAGR] = resource
            }
        }

        fun observeDefaultPreAzanTypeFagr(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_PRE_AZAN_TYPE_FAGR] ?: R.raw.pre_salah_1
                }
        }

        suspend fun getDefaultPreAzanTypeFagr(dataStore: DataStore<Preferences>): Int {
            val performerId =  dataStore.data.first()[DEFAULT_PRE_AZAN_TYPE_FAGR] ?: R.raw.pre_salah_1
            return performerId
        }

        suspend fun setDefaultAzanTypeFagr(dataStore: DataStore<Preferences>, azanType: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_AZAN_TYPE_FAGR] = azanType
            }
        }

        fun observeDefaultAzanTypeFagr(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_AZAN_TYPE_FAGR] ?: R.raw.hamdoon_hamady
                }
        }

        suspend fun getDefaultAzanTypeFagr(dataStore: DataStore<Preferences>): Int {
            val performerId =  dataStore.data.first()[DEFAULT_AZAN_TYPE_FAGR] ?: R.raw.hamdoon_hamady
            return performerId
        }


        suspend fun setDefaultPreAzanTypeDuhur(dataStore: DataStore<Preferences>, resource: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_PRE_AZAN_TYPE_DUHUR] = resource
            }
        }

        fun observeDefaultPreAzanTypeDuhur(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_PRE_AZAN_TYPE_DUHUR] ?: R.raw.pre_salah_1
                }
        }

        suspend fun getDefaultPreAzanTypeDuhur(dataStore: DataStore<Preferences>): Int {
            val performerId =  dataStore.data.first()[DEFAULT_PRE_AZAN_TYPE_DUHUR] ?: R.raw.pre_salah_1
            return performerId
        }

        suspend fun setDefaultAzanTypeDuhur(dataStore: DataStore<Preferences>, azanType: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_AZAN_TYPE_DUHUR] = azanType
            }
        }

        fun observeDefaultAzanTypeDuhur(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_AZAN_TYPE_DUHUR] ?: R.raw.hamdoon_hamady
                }
        }

        suspend fun getDefaultAzanTypeDuhur(dataStore: DataStore<Preferences>): Int {
            val performerId =  dataStore.data.first()[DEFAULT_AZAN_TYPE_DUHUR] ?: R.raw.hamdoon_hamady
            return performerId
        }


        suspend fun setDefaultPreAzanTypeAsr(dataStore: DataStore<Preferences>, resource: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_PRE_AZAN_TYPE_ASR] = resource
            }
        }

        fun observeDefaultPreAzanTypeAsr(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_PRE_AZAN_TYPE_ASR] ?: R.raw.pre_salah_1
                }
        }

        suspend fun getDefaultPreAzanTypeAsr(dataStore: DataStore<Preferences>): Int {
            val performerId =  dataStore.data.first()[DEFAULT_PRE_AZAN_TYPE_ASR] ?: R.raw.pre_salah_1
            return performerId
        }

        suspend fun setDefaultAzanTypeAsr(dataStore: DataStore<Preferences>, azanType: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_AZAN_TYPE_ASR] = azanType
            }
        }

        fun observeDefaultAzanTypeAsr(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_AZAN_TYPE_ASR] ?: R.raw.hamdoon_hamady
                }
        }

        suspend fun getDefaultAzanTypeAsr(dataStore: DataStore<Preferences>): Int {
            val performerId =  dataStore.data.first()[DEFAULT_AZAN_TYPE_ASR] ?: R.raw.hamdoon_hamady
            return performerId
        }

        suspend fun setDefaultPreAzanTypeMaghrib(dataStore: DataStore<Preferences>, resource: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_PRE_AZAN_TYPE_MAGHRIB] = resource
            }
        }

        fun observeDefaultPreAzanTypeMaghrib(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_PRE_AZAN_TYPE_MAGHRIB] ?: R.raw.pre_salah_1
                }
        }

        suspend fun getDefaultPreAzanTypeMaghrib(dataStore: DataStore<Preferences>): Int {
            val performerId =  dataStore.data.first()[DEFAULT_PRE_AZAN_TYPE_MAGHRIB] ?: R.raw.pre_salah_1
            return performerId
        }

        suspend fun setDefaultAzanTypeMaghrib(dataStore: DataStore<Preferences>, azanType: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_AZAN_TYPE_MAGHRIB] = azanType
            }
        }

        fun observeDefaultAzanTypeMaghrib(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_AZAN_TYPE_MAGHRIB] ?: R.raw.hamdoon_hamady
                }
        }

        suspend fun getDefaultAzanTypeMaghrib(dataStore: DataStore<Preferences>): Int {
            val performerId =  dataStore.data.first()[DEFAULT_AZAN_TYPE_MAGHRIB] ?: R.raw.hamdoon_hamady
            return performerId
        }

        suspend fun setDefaultPreAzanTypeIshaa(dataStore: DataStore<Preferences>, resource: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_PRE_AZAN_TYPE_ISHAA] = resource
            }
        }

        fun observeDefaultPreAzanTypeIshaa(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_PRE_AZAN_TYPE_ISHAA] ?: R.raw.pre_salah_1
                }
        }

        suspend fun getDefaultPreAzanTypeIshaa(dataStore: DataStore<Preferences>): Int {
            val performerId =  dataStore.data.first()[DEFAULT_PRE_AZAN_TYPE_ISHAA] ?: R.raw.pre_salah_1
            return performerId
        }
        suspend fun setDefaultAzanTypeIshaa(dataStore: DataStore<Preferences>, azanType: Int){
            dataStore.edit { settings ->
                settings[DEFAULT_AZAN_TYPE_ISHAA] = azanType
            }
        }

        fun observeDefaultAzanTypeIshaa(dataStore: DataStore<Preferences>): Flow<Int> {
            return dataStore.data
                .map { preferences ->
                    preferences[DEFAULT_AZAN_TYPE_ISHAA] ?: R.raw.hamdoon_hamady
                }
        }

        suspend fun getDefaultAzanTypeIshaa(dataStore: DataStore<Preferences>): Int {
            val performerId =  dataStore.data.first()[DEFAULT_AZAN_TYPE_ISHAA] ?: R.raw.hamdoon_hamady
            return performerId
        }

        suspend fun setSummerTime(dataStore: DataStore<Preferences>, isSummerOn: Boolean){
            dataStore.edit { settings ->
                settings[SUMMER_TIME] = isSummerOn
            }
        }

        fun observeSummerTime(dataStore: DataStore<Preferences>): Flow<Boolean> {
            return dataStore.data
                .map { preferences ->
                    preferences[SUMMER_TIME] ?: false
                }
        }

        suspend fun getSummerTime(dataStore: DataStore<Preferences>): Boolean {
            val performerId =  dataStore.data.first()[SUMMER_TIME] ?: false
            return performerId
        }

        suspend fun setLastVersion(dataStore: DataStore<Preferences>, version: Int){
            dataStore.edit { settings ->
                settings[LAST_VERSION] = version
            }
        }

        suspend fun getLastVersion(dataStore: DataStore<Preferences>): Int {
            val lastVersion =  dataStore.data.first()[LAST_VERSION] ?: 0
            return lastVersion
        }

        suspend fun setPageReference(dataStore: DataStore<Preferences>, page: Int){
            dataStore.edit { settings ->
                settings[QURAN_PAGE_REFERENCE] = page
            }
        }

        suspend fun getPageReference(dataStore: DataStore<Preferences>): Int {
            return dataStore.data.first()[QURAN_PAGE_REFERENCE] ?: 1
        }




    }


}