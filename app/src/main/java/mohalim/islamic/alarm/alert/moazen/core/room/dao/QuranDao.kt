package mohalim.islamic.alarm.alert.moazen.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.islamic.alarm.alert.moazen.core.room.entity.QuranPageEntity
import mohalim.islamic.alarm.alert.moazen.core.room.entity.SurahEntity

@Dao
interface QuranDao {
    @Query("SELECT * FROM quran_pages WHERE pageNumber = :pageNumber")
    suspend fun getPage(pageNumber: Int): QuranPageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: QuranPageEntity)

    @Query("SELECT COUNT(*) FROM quran_pages")
    suspend fun getPagesCount(): Int

    @Query("SELECT * FROM surahs")
    suspend fun getAllSurahs(): List<SurahEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurahs(surahs: List<SurahEntity>)

    @Query("SELECT COUNT(*) FROM surahs")
    suspend fun getSurahsCount(): Int
}
