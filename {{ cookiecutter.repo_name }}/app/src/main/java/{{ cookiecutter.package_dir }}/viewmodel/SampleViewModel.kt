package {{ cookiecutter.package_name }}.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import {{ cookiecutter.package_name }}.base.BaseViewModel
import {{ cookiecutter.package_name }}.data.repository.UserRepository
import {{ cookiecutter.package_name }}.model.data.User
import {{ cookiecutter.package_name }}.utils.result.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * ViewModel for [{{ cookiecutter.package_name }}.ui.activity.SampleActivity]
 */
@HiltViewModel
class SampleViewModel @Inject constructor(
    private val userRepository: UserRepository
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

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                userRepository.loadUsers(currentPage)
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    _isLoadingPage.value = false
                    _onNewUserList.value = Event(it)
                    if (currentPage == 1) {
                        _showShimmer.value = false
                    }
                }
            }.onFailure {
                Timber.e(it)
                withContext(Dispatchers.Main) {
                    _isLoadingPage.value = false
                    if (currentPage == 1) {
                        _showShimmer.value = false
                    }
                    currentPage -= 1 // Revert increased count
                }
            }
        }
    }
}
