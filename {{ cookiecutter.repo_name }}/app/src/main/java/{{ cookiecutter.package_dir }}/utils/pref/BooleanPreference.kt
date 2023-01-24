package {{ cookiecutter.package_name }}.utils.pref

import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Shared preference delegated property for boolean values. Use this class to store and retrive
 * boolean value from shared preference just like a normal variable.
 * Usage:
 * ```
 * // Lazy variable for shared preference
 * val lazySharedPreferences = lazy { getSharedPreferences(...) }
 * // Delegated property
 * var isUserLoggedIn by BooleanPreference(lazySharedPreferences, key, defaultValue)
 *
 * // Set value
 * isUserLoggedIn = true
 * // Get value
 * if (isUserLoggedIn)
 *     startHomeScreen()
 * else
 *     startLoginScreen()
 * ```
 */
class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value) }
    }
}
