package mohalim.islamic.alarm.alert.moazen.core.utils

import androidx.datastore.core.DataStore
import java.lang.Exception

sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Success<out T> (val date: T)
    data class Failure(val exception: Throwable) : DataState<Nothing>()
}