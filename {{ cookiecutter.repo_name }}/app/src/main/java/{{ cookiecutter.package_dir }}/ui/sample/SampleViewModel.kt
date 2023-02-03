package {{ cookiecutter.package_name }}.ui.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import {{ cookiecutter.package_name }}.data.remote.response.User
import {{ cookiecutter.package_name }}.data.repository.UserRepository
import {{ cookiecutter.package_name }}.di.IoDispatcher
import {{ cookiecutter.package_name }}.di.MainDispatcher
import {{ cookiecutter.package_name }}.ui.base.BaseViewModel
import {{ cookiecutter.package_name }}.utils.extension.onError
import {{ cookiecutter.package_name }}.utils.extension.onException
import {{ cookiecutter.package_name }}.utils.extension.onSuccess
import {{ cookiecutter.package_name }}.utils.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for [{{ cookiecutter.package_name }}.ui.sample.SampleActivity]
 */
@HiltViewModel
class SampleViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _onNewUserList = MutableLiveData<Event<List<User>>>()
    val onNewUserList: LiveData<Event<List<User>>>
        get() = _onNewUserList

    private val _isLoadingPage = MutableLiveData<Boolean>()
    val isLoadingPage: LiveData<Boolean>
        get() = _isLoadingPage

    private val _showShimmer = MutableLiveData<Boolean>()
    val showShimmer: LiveData<Boolean>
        get() = _showShimmer

    private var currentPage = 0

    init {
        loadMoreUsers()
    }

    fun loadMoreUsers() {
        _isLoadingPage.value = true
        currentPage += 1

        if (currentPage == 1) {
            _showShimmer.value = true
        }

        viewModelScope.launch(ioDispatcher) {
            userRepository
                .loadUsers(currentPage)
                .onSuccess {
                    withContext(mainDispatcher) {
                        _isLoadingPage.value = false
                        _onNewUserList.value = Event(it.results)
                        if (currentPage == 1) {
                            _showShimmer.value = false
                        }
                    }
                }.onError { code, message ->
                    Timber.e(code.toString() + message)
                    withContext(mainDispatcher) { handleApiFailure() }
                }.onException {
                    Timber.e(it.message)
                    withContext(mainDispatcher) { handleApiFailure() }
                }
        }
    }

    /**
     * Handle the failure occuring in API call
     */
    private fun handleApiFailure() {
        _isLoadingPage.value = false
        if (currentPage == 1) {
            _showShimmer.value = false
        }
        currentPage -= 1 // Revert increased count
    }
}
