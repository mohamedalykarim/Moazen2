package mohalim.islamic.alarm.alert.moazen.core.hilt.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mohalim.islamic.alarm.alert.moazen.core.network.interfaces.FileDownloadApi_Interface
import mohalim.islamic.alarm.alert.moazen.core.repository.NetworkRepository
import mohalim.islamic.alarm.alert.moazen.core.repository.RoomRepository
import mohalim.islamic.alarm.alert.moazen.core.room.Database
import mohalim.islamic.alarm.alert.moazen.core.room.dao.AzkarDao
import mohalim.islamic.alarm.alert.moazen.core.room.dao.HadithDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideRoomRepository(
        @ApplicationContext context: Context,
        hadithDao: HadithDao
    ) : RoomRepository {
        return RoomRepository(context, hadithDao)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : Database{
        return Room.databaseBuilder(
            context,
            Database::class.java, Database.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideAzkarDao (database: Database): AzkarDao{
        return database.azkarDao()
    }

    @Singleton
    @Provides
    fun provideHadithDao (database: Database): HadithDao{
        return database.hadithDao()
    }
}