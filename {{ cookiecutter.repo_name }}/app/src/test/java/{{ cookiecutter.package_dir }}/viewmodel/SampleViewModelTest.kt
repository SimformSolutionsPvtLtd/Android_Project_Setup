package {{ cookiecutter.package_name }}.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.capture
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.whenever
import {{ cookiecutter.package_name }}.data.remote.apiresult.ApiError
import {{ cookiecutter.package_name }}.data.remote.apiresult.ApiException
import {{ cookiecutter.package_name }}.data.remote.apiresult.ApiSuccess
import {{ cookiecutter.package_name }}.data.remote.response.User
import {{ cookiecutter.package_name }}.data.remote.response.UserResponse
import {{ cookiecutter.package_name }}.data.repository.UserRepository
import {{ cookiecutter.package_name }}.ui.sample.SampleViewModel
import {{ cookiecutter.package_name }}.utils.StandardDispatcherRule
import {{ cookiecutter.package_name }}.utils.result.Event
import {{ cookiecutter.package_name }}.utils.result.EventObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class SampleViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var standardDispatcherRule = StandardDispatcherRule()

    private val ioDispatcher: TestDispatcher = StandardTestDispatcher()

    private val mainDispatcher: TestDispatcher = StandardTestDispatcher()

    private lateinit var classUnderTest: SampleViewModel

    // Mock dependencies
    @Mock
    private lateinit var mockUserRepository: UserRepository

    // Client Observers
    @Mock
    private lateinit var mockShowShimmerObserver: Observer<Boolean>

    @Mock
    private lateinit var mockIsLoadingPageObserver: Observer<Boolean>

    @Mock
    private lateinit var mockUserListObserver: EventObserver<List<User>>

    // Client Captors
    @Captor
    private lateinit var showShimmerCaptor: ArgumentCaptor<Boolean>

    @Captor
    private lateinit var isLoadingPageCaptor: ArgumentCaptor<Boolean>

    @Captor
    private lateinit var userListCaptor: ArgumentCaptor<Event<List<User>>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        classUnderTest = createClassUnderTest().apply {
            showShimmer.observeForever(mockShowShimmerObserver)
            isLoadingPage.observeForever(mockIsLoadingPageObserver)
            onNewUserList.observeForever(mockUserListObserver)
        }
    }

    @Test
    fun shouldHandleSuccess_whenLoadMoreUsersApiIsSuccessful() = runTest {
        // Given
        val userResponse = createUserResponse()
        whenever(mockUserRepository.loadUsers(anyInt()))
            .thenReturn(ApiSuccess(userResponse))

        // When
        classUnderTest.loadMoreUsers()

        // Ensure coroutines are executed
        ioDispatcher.scheduler.advanceUntilIdle()
        mainDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockShowShimmerObserver, times(2))
            .onChanged(capture(showShimmerCaptor))
        assertThat(showShimmerCaptor.value, IsEqual(false))

        verify(mockIsLoadingPageObserver, times(2))
            .onChanged(capture(isLoadingPageCaptor))
        assertThat(isLoadingPageCaptor.value, IsEqual(false))

        verify(mockUserListObserver).onChanged(capture(userListCaptor))
        assertThat(userListCaptor.value.peekContent(), IsEqual(userResponse.results))
    }

    @Test
    fun shouldHandleError_whenLoadMoreUsersApiThrowsError() = runTest {
        // Given
        whenever(mockUserRepository.loadUsers(anyInt()))
            .thenReturn(ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR, INTERNAL_SERVER_ERROR))

        // When
        classUnderTest.loadMoreUsers()

        // Ensure coroutines are executed
        ioDispatcher.scheduler.advanceUntilIdle()
        mainDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockShowShimmerObserver, times(2))
            .onChanged(capture(showShimmerCaptor))
        assertThat(showShimmerCaptor.value, IsEqual(false))

        verify(mockIsLoadingPageObserver, times(2))
            .onChanged(capture(isLoadingPageCaptor))
        assertThat(isLoadingPageCaptor.value, IsEqual(false))

        verify(mockUserListObserver, never()).onChanged(capture(userListCaptor))
    }

    @Test
    fun shouldHandleError_whenLoadMoreUsersApiThrowsException() = runTest {
        // Given
        whenever(mockUserRepository.loadUsers(anyInt()))
            .thenReturn(ApiException(Throwable(UNKNOWN_HOST, UnknownHostException())))

        // When
        classUnderTest.loadMoreUsers()

        // Ensure coroutines are executed
        ioDispatcher.scheduler.advanceUntilIdle()
        mainDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockShowShimmerObserver, times(2))
            .onChanged(capture(showShimmerCaptor))
        assertThat(showShimmerCaptor.value, IsEqual(false))

        verify(mockIsLoadingPageObserver, times(2))
            .onChanged(capture(isLoadingPageCaptor))
        assertThat(isLoadingPageCaptor.value, IsEqual(false))

        verify(mockUserListObserver, never()).onChanged(capture(userListCaptor))
    }

    @After
    fun tearDown() {
        classUnderTest.apply {
            showShimmer.removeObserver(mockShowShimmerObserver)
            isLoadingPage.removeObserver(mockIsLoadingPageObserver)
            onNewUserList.removeObserver(mockUserListObserver)
        }
    }

    private fun createUserResponse(): UserResponse {
        val users = mutableListOf<User>()
        users.add(User(name = User.Name(title = TITLE, first = FIRST_NAME, last = LAST_NAME)))
        val info = UserResponse.Info(seed = SEED, results = RESULTS, page = PAGE, version = VERSION)
        return UserResponse(users, info)
    }

    private fun createClassUnderTest(): SampleViewModel {
        return SampleViewModel(
            mockUserRepository,
            ioDispatcher,
            mainDispatcher
        )
    }

    companion object {
        // User Response Data
        private const val SEED = "abc"
        private const val RESULTS = 10
        private const val PAGE = 1
        private const val VERSION = "1.4"
        private const val TITLE = "Organization"
        private const val FIRST_NAME = "Simform"
        private const val LAST_NAME = "Solutions"

        // Errors
        private const val INTERNAL_SERVER_ERROR = "Internal Server Error"
        private const val UNKNOWN_HOST = "Unknown Host"
    }
}
