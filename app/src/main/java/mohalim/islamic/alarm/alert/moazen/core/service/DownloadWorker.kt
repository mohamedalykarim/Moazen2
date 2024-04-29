package mohalim.islamic.alarm.alert.moazen.core.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted appContext : Context,
    @Assisted workerParams : WorkerParameters,
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}