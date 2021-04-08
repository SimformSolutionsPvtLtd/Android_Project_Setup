package {{ cookiecutter.package_dir }}.base

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
abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<BaseRecyclerAdapter<T>.RecyclerHolder>(), Filterable {
    private val arrayListFiltered = ArrayList<T>()
    private val itemTypeNormal = 1
    private val itemTypeLoader = 2
    private var filteredText = ""
    protected val arrayList = ArrayList<T>()
    protected val previousArrayList = ArrayList<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val binding: ViewDataBinding = if (viewType == itemTypeNormal) {
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayoutIdForType(viewType), parent, false)
        } else {
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayoutIdForLoading(viewType), parent, false)
        }
        return RecyclerHolder(binding)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun getItemViewType(position: Int): Int = if (isItemLoading(position)) {
        itemTypeLoader
    } else {
        itemTypeNormal
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    /**
     * This is abstract function used to get view type for adapter
     */
    open fun getLayoutIdForType(viewType: Int): Int = 0

    /**
     * This fun is used to get layout for loader.
     * @param viewType Int
     * @return Int
     */
    open fun getLayoutIdForLoading(viewType: Int): Int = 0

    /**
     * This is abstract function used to get item click for all the adapter views
     */
    abstract fun onItemClick(view: View?, adapterPosition: Int)

    /**
     * This is abstract function used to get set data for recycler list items.
     */
    open fun setDataForListItem(binding: ViewDataBinding, data: T) {

    }

    open fun setDataForListItemWithPosition(binding: ViewDataBinding, data: T, adapterPosition: Int) {

    }

    /**
     * This is inner class used to set recycler view holder.
     */
    inner class RecyclerHolder(private val viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBinding.root), View.OnClickListener {
        /**
         * This function is used to bind recycler data particular row wise.
         */
        fun bind(data: T) {
            viewDataBinding.setVariable(BR.data, data)
            viewDataBinding.setVariable(BR.clickHandler, this)
            setDataForListItem(viewDataBinding, data)
            setDataForListItemWithPosition(viewDataBinding, data, adapterPosition)
            viewDataBinding.executePendingBindings()
        }

        override fun onClick(v: View?) {
            onItemClick(v, adapterPosition)
        }
    }

    /**
     * This fun is used to save list
     * @param newList ArrayList<T>
     */
    fun setList(newList: List<T>) {
        val diffResult = DiffUtil.calculateDiff(BaseDiffUtil(arrayList, newList))
        if (arrayList.size >= previousArrayList.size) {
            previousArrayList.clear()
            previousArrayList.addAll(arrayList)
        }
        arrayList.clear()
        arrayList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * This fun is used to add all items in list
     * @param newList ArrayList<T>
     */
    fun addAllItem(newList: List<T>) {
        val newTempList = ArrayList(arrayList)
        newTempList.addAll(newList)
        val diffResult = DiffUtil.calculateDiff(BaseDiffUtil(arrayList, newTempList))
        arrayList.clear()
        arrayList.addAll(newTempList)
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * This fun is used to remove item from list
     * @param item T
     */
    fun removeItem(item: T) {
        val newList = ArrayList(arrayList)
        newList.remove(item)
        setList(newList)
    }

    /**
     * This fun is used to add item
     * @param item T
     */
    fun addItem(item: T) {
        val newList = ArrayList(arrayList)
        newList.add(item)
        setList(newList)
    }

    /**
     * This fun is used to add item at specific position
     * @param index Int
     * @param item T
     */
    fun addItemAt(index: Int, item: T) {
        val newList = ArrayList(arrayList)
        newList.add(index, item)
        setList(newList)
    }

    /**
     * This fun is used to set item at specific position
     * @param index Int
     * @param item T
     */
    fun setItemAt(index: Int, item: T) {
        arrayList[index] = item
        notifyItemChanged(index)
    }

    /**
     * This fun is used to remove item from specific position
     * @param index Int
     */
    fun removeItem(index: Int) {
        val newList = ArrayList(arrayList)
        newList.removeAt(index)
        setList(newList)
    }

    /**
     * This class is used to get all data from adapter
     * @return ArrayList<T>
     */
    fun getListItems(): ArrayList<T> = arrayList

    /**
     * This fun is used to clear list
     */
    fun clearList() {
        val diffResult = DiffUtil.calculateDiff(BaseDiffUtil(arrayList, ArrayList()))
        arrayList.clear()
        arrayList.addAll(ArrayList())
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * This class is used as diff util base class.
     * @property oldList ArrayList<T>
     * @property newList ArrayList<T>
     * @constructor
     */
    inner class BaseDiffUtil(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = areItemsSame(oldList[oldItemPosition], newList[newItemPosition])

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
    }

    /**
     * This fun is used to get list item same or not.
     * @param oldItem T
     * @param newItem T
     * @return Boolean
     */
    abstract fun areItemsSame(oldItem: T, newItem: T): Boolean

    /**
     * This fun is return loader item
     * @return T?
     */
    protected open fun getLoaderItem(): T? = null

    /**
     * This fun is used to add pagination loader.
     */
    fun addLoader() {
        if (!isLoading()) {
            val newList = ArrayList<T>(arrayList)
            getLoaderItem()?.let { newList.add(it) }
            setList(newList)
        }
    }

    /**
     * This fun is used to pagination remove loader
     */
    fun removeLoader() {
        if (isLoading()) {
            if (arrayList.isNotEmpty()) {
                val newList = ArrayList<T>(arrayList)
                newList.remove(getLoaderItem())
                setList(newList)
            }
        }
    }

    /**
     * This fun is used to know item is loading or not.
     * @return Boolean
     */
    internal open fun isLoading(): Boolean = arrayList.isEmpty() || isLastItemLoading()

    /**
     * This fun is used to returns that last item is loading or not
     * @return Boolean
     */
    open fun isLastItemLoading(): Boolean = false

    /**
     * This fun is used to get particular item is loading or not.
     * @param position Int
     * @return Boolean
     */
    open fun isItemLoading(position: Int): Boolean = false

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                filteredText = charSequence.toString()
                if (filteredText.isBlank()) {
                    arrayListFiltered.clear()
                    arrayListFiltered.addAll(previousArrayList)
                } else {
                    arrayListFiltered.clear()
                    arrayListFiltered.addAll(getFilteredResults(filteredText))
                }
                val filterResults = FilterResults()
                filterResults.values = arrayListFiltered
                filterResults.count = arrayListFiltered.size
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                setList(arrayListFiltered)
            }
        }
    }

    /**
     * This fun is used to get filtered result.
     * @param constraint String
     * @return ArrayList<T>
     */
    open fun getFilteredResults(constraint: String): ArrayList<T> = arrayList

}
