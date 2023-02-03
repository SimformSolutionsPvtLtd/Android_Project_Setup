package {{ cookiecutter.package_name }}.data.repository

import {{ cookiecutter.package_name }}.data.remote.ApiService
import {{ cookiecutter.package_name }}.data.remote.apirequest.ApiResult
import {{ cookiecutter.package_name }}.data.remote.response.UserResponse
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    /**
     * Loads [List] of [UserResponse]
     */
    suspend fun loadUsers(page: Int = 1): ApiResult<UserResponse>
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): UserRepository {
    override suspend fun loadUsers(page: Int): ApiResult<UserResponse> =
        apiService.loadUsers(page)
}
