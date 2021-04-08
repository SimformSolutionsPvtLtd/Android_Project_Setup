package {{ cookiecutter.package_dir }}.utils.extension

import com.google.gson.Gson
import {{ cookiecutter.package_dir }}.base.BaseResponse
import retrofit2.HttpException
import retrofit2.Response

const val BAD_REQUEST_CODE = 400

/**
 * Converts [retrofit2.Response] to body response and 
 * handles [BAD_REQUEST_CODE] which returns body response.
 *
 * Provides response if [Response.isSuccessful] or [BAD_REQUEST_CODE].
 * Throws [HttpException] if error is above 400.
 */
inline fun <reified R> Response<R>.response(): R =
    if (isSuccessful) {
        body()!!
    } else {
        if (code() == BAD_REQUEST_CODE) {
            Gson().fromJson(errorBody()?.string(), R::class.java)
        } else {
            throw HttpException(this)
        }
    }
