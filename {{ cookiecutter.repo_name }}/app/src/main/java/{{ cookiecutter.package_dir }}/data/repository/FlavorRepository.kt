package {{ cookiecutter.package_name }}.data.repository

import androidx.lifecycle.LiveData
import {{ cookiecutter.package_name }}.utils.pref.FlavorPreferences
import {{ cookiecutter.package_name }}.utils.ProductFlavor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Flavor repository.
 */
interface FlavorRepository {
    /**
     * Get [ProductFlavor.Flavor].
     */
    fun getFlavor(): ProductFlavor.Flavor

    /**
     * Get [ProductFlavor.Flavor] as [LiveData].
     */
    fun getFlavorLive(): LiveData<ProductFlavor.Flavor>

    /**
     * Set [ProductFlavor.Flavor].
     */
    fun setFlavor(flavor: ProductFlavor.Flavor)
}

@Singleton
class FlavorRepositoryImpl @Inject constructor(
    private val flavorPreferences: FlavorPreferences
) : FlavorRepository {
    override fun getFlavor(): ProductFlavor.Flavor {
        return flavorPreferences.flavor
    }

    override fun getFlavorLive(): LiveData<ProductFlavor.Flavor> {
        return flavorPreferences.observableFlavor
    }

    override fun setFlavor(flavor: ProductFlavor.Flavor) {
        flavorPreferences.flavor = flavor
    }
}
