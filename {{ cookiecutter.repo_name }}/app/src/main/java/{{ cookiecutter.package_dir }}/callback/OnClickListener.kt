package {{ cookiecutter.package_name }}.callback

/**
 * Interface definition for a callback to be invoked when a view is clicked.
 */
interface OnClickListener<T> {
    /**
     * Called when a view is clicked.
     *
     * @param data Pass data
     */
    fun onClick(data: T)
}
