package {{ cookiecutter.package_name }}.data.remote

import {{ cookiecutter.package_name }}.data.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Provides remote APIs
 */
interface ApiService {
    @GET("?inc=name,location,picture,login&results=10&seed=abc")
    suspend fun loadUsers(@Query("page") page: Int): Response<UserResponse>
}
