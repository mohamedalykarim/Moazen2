package mohalim.islamic.alarm.alert.moazen.ui.hadith.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.core.room.dao.HadithDao
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants
import mohalim.islamic.alarm.alert.moazen.core.utils.HadithUtils
import mohalim.islamic.alarm.alert.moazen.ui.hadith.view.HadithViewerActivity
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HadithMainViewModel @Inject constructor(val hadithDao: HadithDao): ViewModel() {
    private val _isRawyDownloaded = MutableStateFlow(false)
    val isRawyDownloaded : StateFlow<Boolean> = _isRawyDownloaded

    fun isFileDownloadInProgress(context: Context): Boolean {
        return try {
            val workManager = WorkManager.getInstance(context)
            val workInfo = workManager.getWorkInfoById(UUID.fromString(Constants.DOWNLOAD_WORKER_MANAGER_UUID)).get()
            workInfo?.let {
                it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun checkIsRawyDownloaded(fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            val hadithCount = hadithDao.getCount(fileName)
            val totalCount = HadithUtils.getHadithCountForRawy(fileName)

            // إذا كان العدد متقارباً جداً (أو متساوٍ)، نعتبره تم التحميل لتجنب مشاكل الفروقات الطفيفة
            // أو نتأكد من مطابقة العدد تماماً
            val isDownloaded = hadithCount >= totalCount && totalCount > 0
            
            _isRawyDownloaded.value = isDownloaded

            Log.d("HadithMainViewModel", "Check Download: $fileName -> DB: $hadithCount, Expected: $totalCount, Result: $isDownloaded")
            return@withContext isDownloaded
        }
    }

    fun startHadithViewerActivity(context: Context, rawy: String) {
        val intent = Intent(context, HadithViewerActivity::class.java).apply {
            putExtra("RAWY_HADITH", rawy)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun toast(context: Context, message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
