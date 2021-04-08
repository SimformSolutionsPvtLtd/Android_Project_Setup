package {{ cookiecutter.package_dir }}.data.repository

import {{ cookiecutter.package_dir }}.data.remote.ApiService
import {{ cookiecutter.package_dir }}.model.data.User
import {{ cookiecutter.package_dir }}.utils.extension.response
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    /**
     * Loads [List] of [User]
     */
    suspend fun loadUsers(page: Int = 1): List<User>
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): UserRepository {
    override suspend fun loadUsers(page: Int): List<User> {
        return apiService.loadUsers(page).response().results
    }
}
