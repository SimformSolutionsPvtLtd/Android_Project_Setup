package {{ cookiecutter.package_name }}.repository

import {{ cookiecutter.package_name }}.data.remote.ApiService
import {{ cookiecutter.package_name }}.data.repository.UserRepository
import {{ cookiecutter.package_name }}.data.repository.UserRepositoryImpl
import {{ cookiecutter.package_name }}.utils.MockResponseFileReader
import {{ cookiecutter.package_name }}.utils.TestApiHelper
import {{ cookiecutter.package_name }}.utils.extension.onError
import {{ cookiecutter.package_name }}.utils.extension.onException
import {{ cookiecutter.package_name }}.utils.extension.onSuccess
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

class UserRepositoryImplTest {

    private lateinit var repository: UserRepository
    private lateinit var apiService: ApiService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        apiService = TestApiHelper.getApiInstance(mockWebServer.url("/").toString())
        repository = UserRepositoryImpl(apiService)
    }

    @Test
    fun loadUsersApiSuccess(): Unit = runTest {
        val expectedResponse = MockResponse().apply {
            setResponseCode(HttpURLConnection.HTTP_OK)
            setBody(MockResponseFileReader().readFile("users.json"))
        }
        mockWebServer.enqueue(expectedResponse)

        repository.loadUsers()
            .onSuccess {
                assertThat(it.results.size, IsEqual(10))
            }
    }

    @Test
    fun loadUsersApiError() = runTest {
        val expectedResponse = MockResponse().apply {
            setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        }
        mockWebServer.enqueue(expectedResponse)

        repository.loadUsers().onError { code, message ->
            assertThat(code, IsEqual(HttpURLConnection.HTTP_INTERNAL_ERROR))
        }
    }

    @Test
    fun loadUsersApiException() = runTest {
        val expectedResponse = MockResponse().apply {
            setResponseCode(HttpURLConnection.HTTP_OK)
            setBody(MockResponseFileReader().readFile(""))
        }
        mockWebServer.enqueue(expectedResponse)

        repository.loadUsers().onException { e ->
            assertThat(e.cause, IsInstanceOf(IllegalStateException::class.java))
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
