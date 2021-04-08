package {{ cookiecutter.package_dir }}.data.local.pref

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import {{ cookiecutter.package_name }}.BuildConfig
import {{ cookiecutter.package_dir }}.utils.ProductFlavor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Flavor shared preferences.
 */
interface FlavorPreferences {
    var flavor: ProductFlavor.Flavor
    val observableFlavor: LiveData<ProductFlavor.Flavor>
}

@Singleton
class FlavorPreferencesImpl @Inject constructor(@ApplicationContext context: Context) : FlavorPreferences {
    companion object {
        private const val NAME = "${BuildConfig.APPLICATION_ID}.flavor"

        object Keys {
            const val FLAVOR = "FLAVOR"
        }
    }

    private val prefs: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(
            NAME, Context.MODE_PRIVATE
        ).apply {
            registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        }
    }

    override var flavor: ProductFlavor.Flavor by FlavorPreference(prefs, Keys.FLAVOR)

    private val _observableFlavor = MutableLiveData<ProductFlavor.Flavor>(ProductFlavor.CURRENT)
    override val observableFlavor: LiveData<ProductFlavor.Flavor>
        get() {
            _observableFlavor.value = flavor
            return _observableFlavor
        }

    private val onSharedPreferenceChangeListener = OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            Keys.FLAVOR -> _observableFlavor.value = flavor
        }
    }

    class FlavorPreference(
        private val preferences: Lazy<SharedPreferences>,
        private val name: String,
        private val defaultValue: ProductFlavor.Flavor = ProductFlavor.CURRENT
    ) : ReadWriteProperty<Any, ProductFlavor.Flavor> {

        @WorkerThread
        override fun getValue(thisRef: Any, property: KProperty<*>): ProductFlavor.Flavor {
            return preferences.value.getString(name, defaultValue.name)?.toProductFlavor() ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: ProductFlavor.Flavor) {
            preferences.value.edit { putString(name, value.name) }
        }

        private fun String.toProductFlavor(): ProductFlavor.Flavor = when (this) {
            ProductFlavor.Flavor.DEV.name -> ProductFlavor.Flavor.DEV
            ProductFlavor.Flavor.QA.name -> ProductFlavor.Flavor.QA
            ProductFlavor.Flavor.PRODUCTION.name -> ProductFlavor.Flavor.PRODUCTION
            else -> ProductFlavor.CURRENT
        }
    }
}
