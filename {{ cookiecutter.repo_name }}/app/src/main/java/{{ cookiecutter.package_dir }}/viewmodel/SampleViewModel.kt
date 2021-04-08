package {{ cookiecutter.package_dir }}.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import {{ cookiecutter.package_dir }}.base.BaseViewModel
import {{ cookiecutter.package_dir }}.data.repository.UserRepository
import {{ cookiecutter.package_dir }}.model.data.User
import {{ cookiecutter.package_dir }}.utils.result.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * ViewModel for [{{ cookiecutter.package_dir }}.ui.activity.SampleActivity]
 */
class SampleViewModel @ViewModelInject constructor(
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
