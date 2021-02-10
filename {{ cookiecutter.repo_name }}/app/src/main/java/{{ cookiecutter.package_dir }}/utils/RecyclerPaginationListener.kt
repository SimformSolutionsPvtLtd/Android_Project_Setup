package {{ cookiecutter.package_name }}.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import {{ cookiecutter.package_name }}.base.BaseRecyclerAdapter

/**
 * Recycler pagination listener invoked [onPageChange] lambda when [itemRemainCount] is reached.
 *
 * @param itemRemainCount Remaining visible item before [onPageChange] invocation
 * @param onPageChange Invoked when page change required
 */
class RecyclerPaginationListener(private val itemRemainCount: Int = 1, private val onPageChange: () -> Unit) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val adapter = recyclerView.adapter as? BaseRecyclerAdapter<*>
        adapter?.let {
            if (!adapter.isLoading()) {
                val linearLayoutManager: LinearLayoutManager? = recyclerView.layoutManager as? LinearLayoutManager
                linearLayoutManager?.let {
                    if (it.findLastCompletelyVisibleItemPosition() >= it.itemCount - itemRemainCount) {
                        onPageChange.invoke()
                    }
                }
            }
        }
    }
}
