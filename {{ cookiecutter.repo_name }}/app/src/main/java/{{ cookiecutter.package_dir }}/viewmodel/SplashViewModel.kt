/*
* Copyright 2021 Test
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package {{ cookiecutter.package_name }}.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import {{ cookiecutter.package_name }}.base.BaseViewModel
import {{ cookiecutter.package_name }}.utils.result.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for [{{ cookiecutter.package_name }}.ui.activity.SplashActivity]
 */
class SplashViewModel @ViewModelInject constructor() : BaseViewModel() {

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
