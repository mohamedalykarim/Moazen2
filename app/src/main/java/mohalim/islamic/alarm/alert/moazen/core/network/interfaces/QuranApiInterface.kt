package mohalim.islamic.alarm.alert.moazen.core.network.interfaces

import mohalim.islamic.alarm.alert.moazen.core.model.quran.PageApi
import mohalim.islamic.alarm.alert.moazen.core.model.quran.QuranApiResponse
import mohalim.islamic.alarm.alert.moazen.core.model.quran.SurahApi
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApiInterface {
    @GET("v1/surah")
    suspend fun getSurahList(): QuranApiResponse<List<SurahApi>>

    @GET("v1/page/{page}/quran-uthmani")
    suspend fun getPage(@Path("page") page: Int): QuranApiResponse<PageApi>
}
