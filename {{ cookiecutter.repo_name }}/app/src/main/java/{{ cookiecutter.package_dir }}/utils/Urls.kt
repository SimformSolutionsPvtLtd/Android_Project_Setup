package {{ cookiecutter.package_dir }}.utils

/**
 * API URLs collection.
 */
object Urls {
    private const val BASE_DEV = "https://randomuser.me/api/"
    private const val BASE_QA = ""
    private const val BASE_PRODUCTION = ""

    /**
     * Get Base URL for [flavor].
     */
    fun getBaseUrl(flavor: ProductFlavor.Flavor): String = when(flavor) {
        ProductFlavor.Flavor.DEV -> BASE_DEV
        ProductFlavor.Flavor.QA -> BASE_QA
        ProductFlavor.Flavor.PRODUCTION -> BASE_PRODUCTION
    }
}
