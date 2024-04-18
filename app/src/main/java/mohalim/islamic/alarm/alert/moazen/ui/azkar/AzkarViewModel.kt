package mohalim.islamic.alarm.alert.moazen.ui.azkar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mohalim.islamic.alarm.alert.moazen.core.room.dao.AzkarDao
import mohalim.islamic.alarm.alert.moazen.core.room.entity.AzkarEntity
import javax.inject.Inject


@HiltViewModel
class AzkarViewModel @Inject constructor(val azkarDao: AzkarDao) : ViewModel(){
    private val _currentZekr = MutableStateFlow<AzkarEntity?>(null)
    val currentZekr : StateFlow<AzkarEntity?> = _currentZekr.asStateFlow()

    val allAzkarlist: MutableList<AzkarEntity> = ArrayList()

    private val _azkar = MutableStateFlow(allAzkarlist)
    val azkar : StateFlow<MutableList<AzkarEntity>> = _azkar.asStateFlow()

    private val _count = MutableStateFlow(0)
    val count : StateFlow<Int> = _count.asStateFlow()


    fun setCurrentZekr(azkarEntity: AzkarEntity){
        _currentZekr.value = azkarEntity
        _count.value = azkarEntity.count
    }

    fun getAllAzkarFromRoom() {
        viewModelScope.launch {
            runBlocking {
                withContext(Dispatchers.IO){
                    try {
                        val azkarRoom = azkarDao.getAll()
                        _azkar.value.clear()
                        _azkar.value.addAll(azkarRoom)

                    }catch (ex: Exception){

                    }
                }
            }
        }
    }

    fun updateZekrCounter(currentZekr: AzkarEntity) {
        viewModelScope.launch {
            runBlocking {
                withContext(Dispatchers.IO){
                    try {
                        azkarDao.updateCount(currentZekr.count +1, currentZekr.id!!)
                        _count.value = currentZekr.count +1
                        _currentZekr.value!!.count = _count.value


                    }catch (ex: Exception){

                    }
                }
            }
        }
    }

    fun resetCounter(currentZekr: AzkarEntity) {
        viewModelScope.launch {
            runBlocking {
                withContext(Dispatchers.IO){
                    try {
                        azkarDao.updateCount(0, currentZekr.id!!)
                        _count.value = 0
                        _currentZekr.value!!.count = 0


                    }catch (ex: Exception){
                        Log.d("TAG", "resetCounter: "+ex.message)
                    }
                }
            }
        }

    }

}