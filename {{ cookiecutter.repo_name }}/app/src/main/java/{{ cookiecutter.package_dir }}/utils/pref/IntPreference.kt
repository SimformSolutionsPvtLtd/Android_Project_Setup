package {{ cookiecutter.package_name }}.utils.pref

import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Shared preference delegated property for int values. Use this class to store and retrive
 * int value from shared preference just like a normal variable.
 * Usage:
 * ```
 * // Lazy variable for shared preference
 * val lazySharedPreferences = lazy { getSharedPreferences(...) }
 * // Delegated property
 * var numberOfRegisterations by IntPreference(lazySharedPreferences, key, defaultValue)
 *
 * // Set value
 * numberOfRegisterations = 1000
 * // Get value
 * if (numberOfRegisterations >= 1000)
 *     onRegisterationsFull()
 * else
 *     onRegisterationsOpen()
 * ```
 */
class IntPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences.value.getInt(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        preferences.value.edit { putInt(name, value) }
    }
}
