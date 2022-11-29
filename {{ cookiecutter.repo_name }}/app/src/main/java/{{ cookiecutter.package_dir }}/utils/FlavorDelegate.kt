package {{ cookiecutter.package_name }}.utils

import androidx.lifecycle.LiveData
import {{ cookiecutter.package_name }}.data.repository.FlavorRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * App flavor delegate.
 *
 * Usage:
 *  In ViewModel:
 *      class YourViewModel @Inject constructor(
 *          flavorDelegate: FlavorDelegate
 *      ): BaseViewModel(), FlavorDelegate by flavorDelegate
 *
 *  In XML:
 *      <TextView
 *          ...
 *          app:showFlavorInfo="@{viewModel.flavor}"
 *          app:flavorDelegate="@{viewModel}"
 *          ...
 *          />
 */
interface FlavorDelegate {
    /**
     * [ProductFlavor.Flavor] [LiveData]
     */
    val flavor: LiveData<ProductFlavor.Flavor>

    /**
     * Set [ProductFlavor.Flavor]
     */
    fun setFlavor(newFlavor: ProductFlavor.Flavor)
}

@Singleton
class FlavorDelegateImpl @Inject constructor(private val flavorRepository: FlavorRepository) : FlavorDelegate {
    override val flavor: LiveData<ProductFlavor.Flavor>
        get() = flavorRepository.getFlavorLive()

    override fun setFlavor(newFlavor: ProductFlavor.Flavor) {
        flavorRepository.setFlavor(newFlavor)
    }
}
