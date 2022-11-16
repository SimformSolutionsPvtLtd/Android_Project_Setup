package {{ cookiecutter.package_name }}.utils.extension

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simformsolutions.app.viewmodel.SplashViewModel

/**
 * Factory for creating the application ViewModels.  Modeled after a similar class in the
 * android architecture components.
 *
 * This creator is to showcase how to inject dependencies into ViewModels.
 *
 * The only parameters supported by this class are application-level, global singletons that any
 * of the ViewModels can have reference to.
 *
 * Add when entries in alphabetical order in appropriate region. Create new region if not available.
 *
 * @param   mApplication    The Android [Application]
 */
class ViewModelFactory private constructor(
    private val mApplication: Application
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = with(modelClass) {
        when {
            // region A
            isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel()
            }

            else -> {
                throw IllegalArgumentException(
                    "Unknown ViewModel class: ${modelClass.name}"
                )
            }
        }
    } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    application
                ).also { INSTANCE = it }
            }
    }
}
