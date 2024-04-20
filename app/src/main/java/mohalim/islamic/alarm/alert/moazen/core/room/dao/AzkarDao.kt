package mohalim.islamic.alarm.alert.moazen.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.islamic.alarm.alert.moazen.core.room.entity.AzkarEntity

@Dao
interface AzkarDao {
    @Query("Select * from azkar")
    fun getAll() : List<AzkarEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNew(azkarEntity: AzkarEntity)


    @Query("UPDATE azkar SET count = :count WHERE id = :id")
    fun updateCount(count: Int, id: Int)
}