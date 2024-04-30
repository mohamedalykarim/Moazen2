package mohalim.islamic.alarm.alert.moazen.ui.hadith.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.ExecutionException
import javax.inject.Inject


@HiltViewModel
class HadithMainViewModel @Inject constructor(): ViewModel() {
    val WORKER_MANAGER_TAG = "WORKER_MANAGER_TAG"


    fun isFileDownloadInProgress(context: Context): Boolean {
        val wm = WorkManager.getInstance(context)
        val statuses:  ListenableFuture<List<WorkInfo>> = wm.getWorkInfosByTag(WORKER_MANAGER_TAG)
        return try {
            var running = false
            val workInfoList: List<WorkInfo> = statuses.get()
            for (workInfo in workInfoList) {
                val state: WorkInfo.State = workInfo.state
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

}