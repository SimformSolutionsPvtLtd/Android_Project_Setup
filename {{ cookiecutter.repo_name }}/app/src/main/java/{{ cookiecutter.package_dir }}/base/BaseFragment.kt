package {{ cookiecutter.package_dir }}.base

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
 * Base fragment for all fragments.
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
        bindViewModel()
        initialize()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initializeObservers(viewModel)
        super.onActivityCreated(savedInstanceState)
    }

    /**
     * Initialize observers for [ViewModel].
     *
     * @param viewModel
     */
    open fun initializeObservers(viewModel: ViewModel) {}

    /**
     * This function will be executed when onCreate() is called.
     */
    open fun initialize() {}

    /**
     * Get the layout resource ID for the screen.
     */
    @LayoutRes
    abstract fun getLayoutResId(): Int

    private fun bindViewModel() {
        binding.apply {
            lifecycleOwner = this@BaseFragment
            setVariable(BR.viewModel, viewModel)
        }
        binding.executePendingBindings()
    }
}
