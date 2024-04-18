package mohalim.islamic.alarm.alert.moazen.core.hilt.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mohalim.islamic.alarm.alert.moazen.core.room.Database
import mohalim.islamic.alarm.alert.moazen.core.room.dao.AzkarDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : Database{
        return Room.databaseBuilder(
            context,
            Database::class.java, Database.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideAzkarDao (database: Database): AzkarDao{
        return database.azkarDao()
    }
}