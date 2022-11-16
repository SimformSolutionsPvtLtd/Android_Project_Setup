package {{ cookiecutter.package_name }}.utils.extension

import com.google.gson.Gson

/**
 * Converts JSON String to POJO.
 */
inline fun <reified T> String.toClass(): T {
    return Gson().fromJson(this, T::class.java)
}

/**
 * Converts any object to JSON string.
 */
inline fun <reified T> T.toJson(): String {
    return Gson().toJson(this)
}
