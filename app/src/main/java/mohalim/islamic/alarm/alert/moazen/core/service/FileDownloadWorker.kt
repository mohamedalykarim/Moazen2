package mohalim.islamic.alarm.alert.moazen.core.service

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import mohalim.islamic.alarm.alert.moazen.core.repository.NetworkRepository

@HiltWorker
class FileDownloadWorker @AssistedInject constructor(
    @Assisted appContext : Context,
    @Assisted workerParams : WorkerParameters,
    val networkRepository: NetworkRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val url = inputData.getString("URL")
        val fileName = inputData.getString("FILE_NAME")

        return try {
            networkRepository.downloadFile(url!!, fileName!!).collect{downloadProgress->
                Log.d("TAG", "doWork: $downloadProgress")
            }
            Result.success()
        }catch (exception : Exception){
            Result.failure()
        }
    }

}