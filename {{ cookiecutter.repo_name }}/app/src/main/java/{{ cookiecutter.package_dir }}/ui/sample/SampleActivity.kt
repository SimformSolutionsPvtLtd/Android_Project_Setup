package {{ cookiecutter.package_name }}.ui.sample

import androidx.activity.viewModels
import {{ cookiecutter.package_name }}.R
import {{ cookiecutter.package_name }}.ui.base.BaseAppCompatActivity
import {{ cookiecutter.package_name }}.databinding.ActivitySampleBinding
import {{ cookiecutter.package_name }}.utils.RecyclerPaginationListener
import {{ cookiecutter.package_name }}.utils.extension.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleActivity : BaseAppCompatActivity<ActivitySampleBinding, SampleViewModel>() {
    override val viewModel: SampleViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.activity_sample

    private val userAdapter = UserAdapter()
    private val paginationListener = RecyclerPaginationListener {
        viewModel.loadMoreUsers()
    }

    override fun initialize() {
        super.initialize()
        binding.rvUsers.adapter = userAdapter
        binding.rvUsers.addOnScrollListener(paginationListener)
    }

    override fun initializeObservers(viewModel: SampleViewModel) {
        super.initializeObservers(viewModel)

        binding.shimmer.startShimmer()

        viewModel.showShimmer.observe(this) {
            if (it) {
                binding.shimmer.startShimmer()
            } else {
                binding.shimmer.stopShimmer()
            }
        }

        viewModel.isLoadingPage.observe(this) {
            if (it) {
                userAdapter.addLoader()
            } else {
                userAdapter.removeLoader()
            }
        }

        viewModel.onNewUserList.observeEvent(this) {
            userAdapter.addAllItem(ArrayList(it))
        }
    }
}
