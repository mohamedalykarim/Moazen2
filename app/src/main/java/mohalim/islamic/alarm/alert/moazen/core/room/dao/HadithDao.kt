package mohalim.islamic.alarm.alert.moazen.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.islamic.alarm.alert.moazen.core.room.entity.HadithEntity

@Dao
interface HadithDao {
    @Query("Select * from hadith")
    fun getAll() : List<HadithEntity>

    @Query("Select * from hadith WHERE number = :number and rawy = :rawy")
    fun getByNumber(rawy: String, number: Int) : HadithEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNew(hadith: HadithEntity): Long

    @Query("Select count(*) from hadith WHERE rawy = :rawy")
    fun getCount(rawy: String) : Int


}