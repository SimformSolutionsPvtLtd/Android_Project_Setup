package {{ cookiecutter.package_name }}.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import {{ cookiecutter.package_name }}.BR

/**
 * Base recycler adapter for all recycler adapters.
 */
@Suppress("TooManyFunctions")
abstract class BaseRecyclerAdapter<T> :
    RecyclerView.Adapter<BaseRecyclerAdapter<T>.RecyclerHolder>(), Filterable {
    /**
     * Inner class to set recycler view holder
     *
     * @param viewDataBinding   The [ViewDataBinding] instance
     */
    inner class RecyclerHolder(
        private val viewDataBinding: ViewDataBinding
    ) : RecyclerView.ViewHolder(viewDataBinding.root), View.OnClickListener {
        /**
         * Bind the holder with [data]
         *
         * @param data  The data to bind
         */
        fun bind(data: T) {
            viewDataBinding.setVariable(BR.data, data)
            viewDataBinding.setVariable(BR.clickHandler, this)
            setDataForListItem(viewDataBinding, data)
            setDataForListItemWithPosition(viewDataBinding, data, absoluteAdapterPosition)
            viewDataBinding.executePendingBindings()
        }

        override fun onClick(v: View?) {
            onItemClick(v, absoluteAdapterPosition)
        }
    }

    /**
     * Base class for [DiffUtil] to calculate difference between old and new list
     *
     * @param oldList   The old list
     * @param newList   The new list
     */
    inner class BaseDiffUtil(
        private val oldList: List<T>,
        private val newList: List<T>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsSame(oldList[oldItemPosition], newList[newItemPosition])

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areContentsSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    private val arrayListFiltered = ArrayList<T>()
    private var filteredText = ""
    protected val arrayList = ArrayList<T>()
    protected val previousArrayList = ArrayList<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getLayoutIdForType(viewType),
                parent,
                false
            )
        return RecyclerHolder(binding)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun getItemViewType(position: Int): Int = if (isItemLoading(position)) {
        ITEM_TYPE_LOADER
    } else {
        ITEM_TYPE_NORMAL
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            filteredText = charSequence.toString()
            arrayListFiltered.clear()
            arrayListFiltered.addAll(
                if (filteredText.isBlank()) previousArrayList else getFilteredResults(filteredText)
            )
            return FilterResults().apply { values = arrayListFiltered }
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            setItems(arrayListFiltered)
        }
    }

    /**
     * Get the layout id for required [viewType]
     *
     * @param viewType  The view type to get layout id
     *
     * @return The layout id of the required view type
     */
    abstract fun getLayoutIdForType(viewType: Int): Int

    /**
     * Callback on item clicked
     *
     * @param view      The view that is clicked
     * @param position  The position of the [view] clicked
     */
    abstract fun onItemClick(view: View?, position: Int)

    /**
     * Compares [firstItem] with [secondItem]
     *
     * @param firstItem     First item
     * @param secondItem    Second item
     *
     * @return A [Boolean] representing true if both items are same, otherwise false
     */
    abstract fun areItemsSame(firstItem: T, secondItem: T): Boolean

    /**
     * Compares the contents of the [firstItem] with [secondItem]
     *
     * @param firstItem     First item
     * @param secondItem    Second item
     *
     * @return A [Boolean] representing true if contents of both items are same, otherwise false
     */
    protected open fun areContentsSame(firstItem: T, secondItem: T): Boolean =
        firstItem == secondItem

    /**
     * Set the list item with [data]
     *
     * @param binding   The [ViewDataBinding] instance
     * @param data      The data to set
     */
    protected open fun setDataForListItem(binding: ViewDataBinding, data: T) { }

    /**
     * Set the list item with [data] at [position]
     *
     * @param binding   The [ViewDataBinding] instance
     * @param data      The data to set
     * @param position  The position at which [data] should be added
     */
    protected open fun setDataForListItemWithPosition(
        binding: ViewDataBinding,
        data: T,
        position: Int
    ) { }

    /**
     * Get the loader item
     *
     * @return The loader item
     */
    protected open fun getLoaderItem(): T? = null

    /**
     * Check if item is loading or not
     *
     * @return A [Boolean] representing true if item is loading, otherwise false
     */
    internal open fun isLoading(): Boolean = arrayList.isEmpty() || isLastItemLoading()

    /**
     * Check if last item is loading or not
     *
     * @return A [Boolean] representing true if last item is loading, otherwise false
     */
    open fun isLastItemLoading(): Boolean = false

    /**
     * Check if item at [index] is loading or not
     *
     * @param index Index of the item to check
     *
     * @return A [Boolean] representing true if item is loading, otherwise false
     */
    open fun isItemLoading(index: Int): Boolean = false

    /**
     * Get the filtered results
     *
     * @param filteredText  The text to filter
     *
     * @return The [ArrayList] of filtered list
     */
    open fun getFilteredResults(filteredText: String): ArrayList<T> = arrayList

    // Region add item(s)
    /**
     * Add the [item]
     *
     * @param item  The item to add
     */
    fun addItem(item: T) =
        setItems(arrayList.toMutableList().apply { add(item) })

    /**
     * Add the [item] at [index]
     *
     * @param item  The item to add
     * @param index Index of the [item]
     */
    fun addItemAt(index: Int, item: T) =
        setItems(arrayList.toMutableList().apply { add(index, item) })

    /**
     * Add all the items in recycler view
     *
     * @param newList   The [List] to add
     */
    fun addAllItem(newList: List<T>) =
        setItems(arrayList.toMutableList().apply { addAll(newList) })

    /**
     * Add the pagination loader
     */
    fun addLoader() {
        if (isLoading()) return
        getLoaderItem()?.let {
            setItems(arrayList.toMutableList().apply { add(it) })
        }
    }
    // End region

    // Region remove item(s)
    /**
     * Remove the [item]
     *
     * @param item  The item to remove
     */
    fun removeItem(item: T) =
        setItems(arrayList.toMutableList().apply { remove(item) })

    /**
     * Remove the item at [index]
     *
     * @param index The index of item to remove
     */
    fun removeItemAt(index: Int) =
        setItems(arrayList.toMutableList().apply { removeAt(index) })

    /**
     * Remove all the items
     */
    fun removeAllItems() = setItems(listOf())

    /**
     * Remove the pagination loader
     */
    fun removeLoader() {
        if (!isLoading()) return
        if (arrayList.isEmpty()) return
        getLoaderItem()?.let {
            setItems(arrayList.toMutableList().apply { remove(it) })
        }
    }
    // End region

    /**
     * Update the item at [index]
     *
     * @param item  New item to set
     * @param index Index of the [item]
     */
    fun updateItemAt(index: Int, item: T) {
        arrayList[index] = item
        notifyItemChanged(index)
    }

    /**
     * Sets the list in recycler view
     *
     * @param newList   The [List] to set
     */
    private fun setItems(newList: List<T>) {
        if (arrayList.size >= previousArrayList.size) {
            previousArrayList.clear()
            previousArrayList.addAll(arrayList)
        }
        DiffUtil.calculateDiff(BaseDiffUtil(arrayList, newList)).apply {
            arrayList.clear()
            arrayList.addAll(newList)
            dispatchUpdatesTo(this@BaseRecyclerAdapter)
        }
    }

    companion object {
        const val ITEM_TYPE_NORMAL = 1
        const val ITEM_TYPE_LOADER = 2
    }
}
