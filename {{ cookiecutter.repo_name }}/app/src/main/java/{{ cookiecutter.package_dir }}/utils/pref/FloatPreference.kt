package {{ cookiecutter.package_name }}.utils.pref

import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Shared preference delegated property for float values. Use this class to store and retrive
 * float value from shared preference just like a normal variable.
 * Usage:
 * ```
 * // Lazy variable for shared preference
 * val lazySharedPreferences = lazy { getSharedPreferences(...) }
 * // Delegated property
 * var percentComplete by FloatPreference(lazySharedPreferences, key, defaultValue)
 *
 * // Set value
 * percentComplete = 100f
 * // Get value
 * if (percentComplete == 100f)
 *     showTutorialCompleteDialog()
 * else
 *     showTutorialIncompleteDialog()
 * ```
 */
class FloatPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Float
) : ReadWriteProperty<Any, Float> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Float {
        return preferences.value.getFloat(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
        preferences.value.edit { putFloat(name, value) }
    }
}
