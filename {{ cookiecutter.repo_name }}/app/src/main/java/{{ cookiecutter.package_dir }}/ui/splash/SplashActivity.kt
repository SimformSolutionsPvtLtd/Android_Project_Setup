package {{ cookiecutter.package_name }}.ui.splash

import androidx.activity.viewModels
import {{ cookiecutter.package_name }}.R
import {{ cookiecutter.package_name }}.ui.base.BaseAppCompatActivity
import {{ cookiecutter.package_name }}.databinding.ActivitySplashBinding
import {{ cookiecutter.package_name }}.ui.sample.SampleActivity
import {{ cookiecutter.package_name }}.utils.extension.launchActivity
import {{ cookiecutter.package_name }}.utils.extension.observeEvent
import {{ cookiecutter.package_name }}.ui.splash.SplashViewModel.Destination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseAppCompatActivity<ActivitySplashBinding, SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.activity_splash

    override fun setupViewModel() {
        super.setupViewModel()

        viewModel.goToScreen.observeEvent(this) { destination ->
            when (destination) {
                Destination.Home -> openHomeScreen()
                Destination.Login -> openLoginScreen()
                Destination.Sample -> openSampleScreen()
            }
        }
    }

    private fun openLoginScreen() {
        // TODO : Open Login screen
    }

    private fun openHomeScreen() {
        // TODO : Open Home screen
    }

    private fun openSampleScreen() {
        launchActivity<SampleActivity>()
    }
}
