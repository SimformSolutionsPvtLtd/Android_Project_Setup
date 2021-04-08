package {{ cookiecutter.package_name }}.base

/**
 * Base API response.
 *
 * @property status `true` if success
 * @property message Error message if any
 * @property data Data
 *
 * Usage:
 *
 *  data class LoginResponse(
 *      override val data: LoginResponseData
 *  ) : BaseResponse<LoginResponseData>()
 *
 *  data class LoginResponseData(
 *      val userId: String
 *  )
 */
abstract class BaseResponse<D : Any> {
    val status: Boolean = false

    val message: String = ""

    abstract val data: D

    override fun toString(): String {
        return "Status: $status\nMessage: $message\nData: $data"
    }
}

/**
 * Executes [onSuccess] when [BaseResponse.status] is `true`.
 *
 * @param onSuccess Invoked when [BaseResponse.status] is `true`
 */
inline fun <D : Any> BaseResponse<D>.onSuccess(crossinline onSuccess: (D) -> Unit) {
    if (status) {
        onSuccess(data)
    }
}

/**
 * Executes [onSuccess] when [BaseResponse.status] is `true`.
 * Suspend function with suspend [onSuccess].
 *
 * @param onSuccess Invoked when [BaseResponse.status] is `true`
 */
suspend inline fun <D : Any> BaseResponse<D>.suspendOnSuccess(crossinline onSuccess: suspend (D) -> Unit) {
    if (status) {
        onSuccess(data)
    }
}
