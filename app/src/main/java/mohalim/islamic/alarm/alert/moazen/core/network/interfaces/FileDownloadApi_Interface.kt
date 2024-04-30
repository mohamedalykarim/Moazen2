package mohalim.islamic.alarm.alert.moazen.core.network.interfaces

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileDownloadApi_Interface {
    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String) : Response<ResponseBody>
}