package mohalim.islamic.alarm.alert.moazen.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import mohalim.islamic.alarm.alert.moazen.core.room.dao.AzkarDao
import mohalim.islamic.alarm.alert.moazen.core.room.dao.HadithDao
import mohalim.islamic.alarm.alert.moazen.core.room.entity.AzkarEntity
import mohalim.islamic.alarm.alert.moazen.core.room.entity.HadithEntity

@Database(entities = [AzkarEntity::class, HadithEntity::class], version = 5, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun azkarDao() : AzkarDao
    abstract fun hadithDao() : HadithDao

    companion object{
        const val DATABASE_NAME = "moazen_database"
    }
}