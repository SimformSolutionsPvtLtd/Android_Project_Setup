package {{ cookiecutter.package_name }}.utils.extension

import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.optString(name: String, default: String?): String? {
    return if (has(name)) getString(name) else default
}

fun JSONObject.optInt(name: String, default: Int?): Int? {
    return if (has(name)) getInt(name) else default
}

fun JSONObject.optBoolean(name: String, default: Boolean?): Boolean? {
    return if (has(name)) getBoolean(name) else default
}

fun JSONObject.optDouble(name: String, default: Double?): Double? {
    return if (has(name)) getDouble(name) else default
}

fun JSONObject.optLong(name: String, default: Long?): Long? {
    return if (has(name)) getLong(name) else default
}

fun JSONObject.optJSONObject(name: String, default: JsonObject?): JSONObject? {
    return if (has(name)) getJSONObject(name) else null
}

fun JSONObject.optJSONArray(name: String, default: JSONArray?): JSONArray? {
    return if (has(name)) getJSONArray(name) else default
}

fun JSONArray.toArrayList(): ArrayList<String> {
    val list = arrayListOf<String>()
    for (i in 0 until this.length()) {
        list.add(this.getString(i))
    }
    return list
}
