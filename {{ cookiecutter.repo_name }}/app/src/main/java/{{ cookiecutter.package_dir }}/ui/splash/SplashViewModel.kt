package {{ cookiecutter.package_name }}.ui.splash


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import {{ cookiecutter.package_name }}.ui.base.BaseViewModel
import {{ cookiecutter.package_name }}.utils.result.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * ViewModel for [{{ cookiecutter.package_name }}.ui.splash.SplashActivity]
 */
@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {

    companion object {
        private const val LAUNCH_DELAY = 3000L
    }

    private val _goToScreen = MutableLiveData<Event<Destination>>()
    val goToScreen: LiveData<Event<Destination>>
        get() = _goToScreen

    init {
        navigateTo()
    }

    /**
     * Decides where to navigate user.
     */
    private fun navigateTo() {
        viewModelScope.launch {
            delay(LAUNCH_DELAY)
            // TODO : Add logic to decide destination
            _goToScreen.value = Event(Destination.Sample)
        }
    }

    sealed class Destination {
        object Home : Destination()
        object Login : Destination()
        object Sample : Destination()
    }
}
