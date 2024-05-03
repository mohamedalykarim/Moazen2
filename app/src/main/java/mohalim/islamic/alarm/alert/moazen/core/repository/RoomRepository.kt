package mohalim.islamic.alarm.alert.moazen.core.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.islamic.alarm.alert.moazen.core.model.DownloadProgress
import mohalim.islamic.alarm.alert.moazen.core.room.dao.HadithDao
import mohalim.islamic.alarm.alert.moazen.core.room.entity.HadithEntity
import mohalim.islamic.alarm.alert.moazen.core.utils.DataState
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class RoomRepository @Inject constructor(val context: Context, val hadithDao: HadithDao) : RoomRepositoryInterface {
    override suspend fun getCurrentHadith(rawyfileName: String, page: Int): Flow<HadithEntity> {
        return flow {
            try {
                val current = hadithDao.getByNumber(rawyfileName, page)
                if (current != null){
                    emit(current)
                }
            }catch (exception: Exception){

            }
        }.flowOn(Dispatchers.IO)
    }

}