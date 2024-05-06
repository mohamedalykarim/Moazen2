package mohalim.islamic.alarm.alert.moazen.ui.hadith.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableFloatState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.R
import mohalim.islamic.alarm.alert.moazen.core.room.dao.HadithDao
import mohalim.islamic.alarm.alert.moazen.core.service.FileDownloadWorker
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.core.utils.HadithUtils
import mohalim.islamic.alarm.alert.moazen.ui.hadith.view.HadithViewerActivity
import java.util.UUID
import java.util.concurrent.ExecutionException
import javax.inject.Inject


@HiltViewModel
class HadithMainViewModel @Inject constructor(val hadithDao: HadithDao): ViewModel() {
    private val _isRawyDownloaded = MutableStateFlow(false)
    val isRawyDownloaded : MutableStateFlow<Boolean> = _isRawyDownloaded

    fun isFileDownloadInProgress(context: Context): Boolean {
        val workManager = WorkManager.getInstance(context)
        val runningWorker = workManager.getWorkInfoById(UUID.fromString(Constants.DOWNLOAD_WORKER_MANAGER_UUID)).get()
        if (runningWorker == null) {
            Log.d("HadithMainViewModel", "isFileDownloadInProgress: runningWorker null")
            return false
        }else{
            Log.d("HadithMainViewModel", "isFileDownloadInProgress: RUNNING "+runningWorker.state)
            return runningWorker.state == WorkInfo.State.ENQUEUED || runningWorker.state == WorkInfo.State.RUNNING
        }
    }

    suspend fun isRawyDownloaded(fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            val hadithCount = hadithDao.getCount(fileName)
            val totalCount = HadithUtils.getHadithCountForRawy(fileName)

            _isRawyDownloaded.value = hadithCount == totalCount

            return@withContext hadithCount == totalCount
        }
    }

    fun startHadithViewerActivity(context: Context, rawy: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                val intent = Intent(context, HadithViewerActivity::class.java)
                intent.putExtra("RAWY_HADITH", rawy)
                context.startActivity(intent)
            }
        }

    }

    fun toast(context: Context, message: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}