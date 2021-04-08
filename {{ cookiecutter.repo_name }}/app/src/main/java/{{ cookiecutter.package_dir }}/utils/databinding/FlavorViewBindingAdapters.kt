package {{ cookiecutter.package_dir }}.utils.databinding

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import {{ cookiecutter.package_name }}.BuildConfig
import {{ cookiecutter.package_dir }}.ui.delegate.FlavorDelegate
import {{ cookiecutter.package_dir }}.utils.ProductFlavor
import {{ cookiecutter.package_dir }}.utils.ProductFlavor.Flavor

@BindingAdapter("showFlavorInfo", "flavorDelegate")
fun showFlavorInfo(view: TextView, flavor: Flavor?, delegate: FlavorDelegate) {
    if (flavor == null) {
        view.isVisible = false
        return
    }

    if (ProductFlavor.CURRENT == Flavor.DEV || ProductFlavor.CURRENT == Flavor.QA) {
        val message = "Environment: ${flavor.name}\n${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
        view.text = message
        view.isVisible = true
        val allFlavor = Flavor::class.sealedSubclasses.mapNotNull { it.objectInstance }
        val flavors = allFlavor.map { it.name }.toTypedArray()
        view.setOnClickListener {
            MaterialAlertDialogBuilder(view.context)
                .setSingleChoiceItems(flavors, allFlavor.indexOf(flavor)) { dialog, which ->
                    val newFlavor = allFlavor[which]
                    delegate.setFlavor(newFlavor)
                    dialog.dismiss()
                }
                .show()
        }
    } else {
        view.isVisible = false
    }
}
