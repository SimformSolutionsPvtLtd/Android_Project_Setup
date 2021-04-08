package {{ cookiecutter.package_name }}.ui.adapter

import android.view.View
import {{ cookiecutter.package_name }}.R
import {{ cookiecutter.package_name }}.base.BaseRecyclerAdapter
import {{ cookiecutter.package_name }}.model.data.User

class UserAdapter : BaseRecyclerAdapter<User>() {
    override fun getLayoutIdForType(viewType: Int): Int = R.layout.item_user

    override fun onItemClick(view: View?, adapterPosition: Int) {
        /* no-op */
    }

    override fun areItemsSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun getLayoutIdForLoading(viewType: Int): Int = R.layout.layout_loader

    override fun isLastItemLoading(): Boolean = arrayList.lastOrNull()?.login?.uuid.isNullOrBlank()

    override fun isItemLoading(position: Int): Boolean = arrayList[position].login.uuid.isBlank()

    override fun getLoaderItem(): User = User()
}
