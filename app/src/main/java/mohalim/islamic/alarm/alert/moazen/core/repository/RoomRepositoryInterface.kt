package mohalim.islamic.alarm.alert.moazen.core.repository

import kotlinx.coroutines.flow.Flow
import mohalim.islamic.alarm.alert.moazen.core.model.DownloadProgress
import mohalim.islamic.alarm.alert.moazen.core.room.entity.HadithEntity
import mohalim.islamic.alarm.alert.moazen.core.utils.DataState
import okhttp3.ResponseBody
import retrofit2.Response

interface RoomRepositoryInterface {
    suspend fun getCurrentHadith(rawyfileName: String, page: Int) : Flow<HadithEntity>
}