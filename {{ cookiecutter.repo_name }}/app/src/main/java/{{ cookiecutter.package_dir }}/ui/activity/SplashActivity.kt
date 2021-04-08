package {{ cookiecutter.package_dir }}.ui.activity

import androidx.activity.viewModels
import {{ cookiecutter.package_name }}.R
import {{ cookiecutter.package_dir }}.base.BaseAppCompatActivity
import {{ cookiecutter.package_name }}.databinding.ActivitySplashBinding
import {{ cookiecutter.package_dir }}.utils.extension.launchActivity
import {{ cookiecutter.package_dir }}.utils.extension.observeEvent
import {{ cookiecutter.package_dir }}.viewmodel.SplashViewModel
import {{ cookiecutter.package_dir }}.viewmodel.SplashViewModel.Destination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseAppCompatActivity<ActivitySplashBinding, SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.activity_splash

    override fun initializeObservers(viewModel: SplashViewModel) {
        super.initializeObservers(viewModel)

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
