package mohalim.islamic.alarm.alert.moazen.core.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.islamic.alarm.alert.moazen.core.model.DownloadProgress
import mohalim.islamic.alarm.alert.moazen.core.network.interfaces.FileDownloadApi_Interface
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class NetworkRepository @Inject constructor(val context: Context, val fileDownloadInterface: FileDownloadApi_Interface) : NetworkRepositoryInterface {
    override suspend fun downloadFile(url: String, fileName: String): Flow<DownloadProgress> {
        return flow {
            val response = fileDownloadInterface.downloadFile(url)
            val file = File(context.filesDir, fileName)
            val fileOutputStream = FileOutputStream(file)
            val totalBytes = response.body()!!.contentLength()
            var downloadedBytes = 0L

            response.body()?.byteStream()?.use { inputStream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead)
                    downloadedBytes += bytesRead.toLong()
                    val progress = (downloadedBytes * 100) / totalBytes
                    emit(DownloadProgress(progress.toInt()))
                }
            }

        }.flowOn(Dispatchers.IO)
    }

}