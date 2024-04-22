package mohalim.islamic.alarm.alert.moazen.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import mohalim.islamic.alarm.alert.moazen.core.room.dao.AzkarDao
import mohalim.islamic.alarm.alert.moazen.core.room.entity.AzkarEntity

@Database(entities = [AzkarEntity::class], version = 4, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun azkarDao() : AzkarDao

    companion object{
        const val DATABASE_NAME = "moazen_database"
    }
}