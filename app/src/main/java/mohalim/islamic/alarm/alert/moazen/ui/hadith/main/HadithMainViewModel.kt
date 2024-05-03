package mohalim.islamic.alarm.alert.moazen.ui.hadith.main

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.core.room.dao.HadithDao
import mohalim.islamic.alarm.alert.moazen.core.utils.HadithUtils
import mohalim.islamic.alarm.alert.moazen.ui.hadith.view.HadithViewerActivity
import java.util.concurrent.ExecutionException
import javax.inject.Inject


@HiltViewModel
class HadithMainViewModel @Inject constructor(val hadithDao: HadithDao): ViewModel() {
    val WORKER_MANAGER_TAG = "WORKER_MANAGER_TAG"


    fun isFileDownloadInProgress(context: Context): Boolean {
        val wm = WorkManager.getInstance(context)
        val statuses:  ListenableFuture<List<WorkInfo>> = wm.getWorkInfosByTag(WORKER_MANAGER_TAG)
        return try {
            var running = false
            val workInfoList: List<WorkInfo> = statuses.get()
            for (workInfo in workInfoList) {
                val state: WorkInfo.State = workInfo.state
                Log.d("HadithMainViewModel", "isFileDownloadInProgress:  $state")
                running = (state == WorkInfo.State.RUNNING) or (state == WorkInfo.State.ENQUEUED)
            }
            running
        } catch (e: ExecutionException) {
            e.printStackTrace()
            false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun isRawyDownloaded(fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            val hadithCount = hadithDao.getCount(fileName)
            val totalCount = HadithUtils.getHadithCountForRawy(fileName)

            Log.d("HadithMainViewModel", "isRawyDownloaded: $hadithCount" + " totalCount $totalCount")
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

}