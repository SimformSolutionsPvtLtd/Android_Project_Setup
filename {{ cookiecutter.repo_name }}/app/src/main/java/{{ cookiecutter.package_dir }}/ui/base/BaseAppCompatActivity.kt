package {{ cookiecutter.package_name }}.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import {{ cookiecutter.package_name }}.BR

/**
 * Base activity for all activities
 */
abstract class BaseAppCompatActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel> :
    AppCompatActivity() {

    protected lateinit var binding: Binding
    protected abstract val viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutResId())
        setupUi()
        setupViewModel()
        initialize()
    }

    /**
     * Get the layout resource ID for the activity
     */
    @LayoutRes
    abstract fun getLayoutResId(): Int

    /**
     * Setup [ViewModel]
     */
    open fun setupViewModel() {}

    /**
     * Called when onCreate() is executed.
     * This method will be called after setupUi() and setupViewModel() has been executed
     * @see setupUi
     * @see setupViewModel
     */
    open fun initialize() {}

    /**
     * Setup the UI
     */
    private fun setupUi() = with(binding) {
        lifecycleOwner = this@BaseAppCompatActivity
        setVariable(BR.viewModel, viewModel)
        executePendingBindings()
    }
}
