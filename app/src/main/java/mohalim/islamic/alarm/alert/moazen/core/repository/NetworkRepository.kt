package mohalim.islamic.alarm.alert.moazen.core.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.islamic.alarm.alert.moazen.core.model.DownloadProgress
import mohalim.islamic.alarm.alert.moazen.core.room.entity.HadithEntity
import mohalim.islamic.alarm.alert.moazen.core.network.interfaces.FileDownloadApi_Interface
import mohalim.islamic.alarm.alert.moazen.core.room.dao.HadithDao
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.core.utils.HadithUtils
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class NetworkRepository @Inject constructor(val context: Context, val fileDownloadInterface: FileDownloadApi_Interface, val hadithDao: HadithDao) : NetworkRepositoryInterface {
    override suspend fun downloadFile(url: String, fileName: String): Flow<DownloadProgress> {
        return flow {
            try {
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
                        emit(DownloadProgress(Constants.DOWNLOAD_PROCESS_TYPE_DOWNLOADING, progress.toInt()))
                    }
                }

                fileOutputStream.close()

                val bufferedReader = context.openFileInput(fileName).bufferedReader()
                val hadithList : MutableList<HadithEntity> = Gson().fromJson(bufferedReader, object : TypeToken<MutableList<HadithEntity>>() {}.type)

                var index = 1
                for (hadith in hadithList) {
                    hadith.rawy = fileName.replace(".json", "")
                    val progress = ((index * 100).toLong() / hadithList.size.toLong()).toInt()
                    emit(DownloadProgress(Constants.DOWNLOAD_PROCESS_TYPE_INSTALLING, progress))
                    hadithDao.addNew(hadith)
                    index++
                }

                HadithUtils.removeFile(context, fileName)

            }catch (exception :Exception){
                Log.e("NetworkRepository", "downloadFile: ${exception.message}")
            }

        }.flowOn(Dispatchers.IO)
    }

}