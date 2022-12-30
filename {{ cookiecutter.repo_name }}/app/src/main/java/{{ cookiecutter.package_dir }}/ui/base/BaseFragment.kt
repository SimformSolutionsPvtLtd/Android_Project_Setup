package {{ cookiecutter.package_name }}.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import {{ cookiecutter.package_name }}.BR

/**
 * Base fragment for all fragments
 */
abstract class BaseFragment<Binding : ViewDataBinding, ViewModel : BaseViewModel> : Fragment() {

    protected lateinit var binding: Binding
    protected abstract val viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupViewModel()
        initialize()
    }

    /**
     * Get the layout resource ID for the fragment
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
        lifecycleOwner = this@BaseFragment
        setVariable(BR.viewModel, viewModel)
        executePendingBindings()
    }
}
