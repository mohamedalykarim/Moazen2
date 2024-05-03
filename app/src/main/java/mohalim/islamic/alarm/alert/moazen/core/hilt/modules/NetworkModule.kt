package mohalim.islamic.alarm.alert.moazen.core.hilt.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mohalim.islamic.alarm.alert.moazen.core.network.interfaces.FileDownloadApi_Interface
import mohalim.islamic.alarm.alert.moazen.core.repository.NetworkRepository
import mohalim.islamic.alarm.alert.moazen.core.room.dao.HadithDao
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideRetrofit() : Retrofit.Builder{
        val client = OkHttpClient.Builder().build()
        return Retrofit.Builder().baseUrl("https://drive.google.com").client(client)
    }

    @Provides
    fun provideFileDownloadApiInterface(retrofit : Retrofit.Builder): FileDownloadApi_Interface {
        return retrofit.build().create(FileDownloadApi_Interface::class.java)
    }

    @Provides
    fun provideNetworkRepository(
        @ApplicationContext context: Context,
        fileDownloadInterface : FileDownloadApi_Interface,
        hadithDao: HadithDao
    ) : NetworkRepository{
        return NetworkRepository(context, fileDownloadInterface, hadithDao)
    }


}