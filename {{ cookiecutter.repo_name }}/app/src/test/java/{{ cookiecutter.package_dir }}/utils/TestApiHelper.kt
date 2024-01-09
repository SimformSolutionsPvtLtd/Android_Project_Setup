package {{ cookiecutter.package_name }}.utils

import com.google.gson.GsonBuilder
import {{ cookiecutter.package_name }}.data.remote.ApiService
import {{ cookiecutter.package_name }}.data.remote.apiresult.ApiResultCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TestApiHelper {

    fun getApiInstance(baseUrl: String): ApiService {
        val okHttpClient = OkHttpClient.Builder().build()
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(ApiResultCallAdapterFactory())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build().create(ApiService::class.java)
    }
}
