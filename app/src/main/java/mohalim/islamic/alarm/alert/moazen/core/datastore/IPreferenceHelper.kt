package mohalim.islamic.alarm.alert.moazen.core.datastore

interface IPreferenceHelper {
    suspend fun setIsFirstOpen(isFirstOpen : Boolean)
    suspend fun getIsFirstOpen() : Boolean
}