package {{ cookiecutter.package_name }}.utils.extension

import {{ cookiecutter.package_name }}.data.remote.apiresult.ApiError
import {{ cookiecutter.package_name }}.data.remote.apiresult.ApiException
import {{ cookiecutter.package_name }}.data.remote.apiresult.ApiResult
import {{ cookiecutter.package_name }}.data.remote.apiresult.ApiSuccess

/**
 * Execute [executable] if the [ApiResult] is of type [ApiResult.ApiSuccess]
 *
 * @param executable    Block to execute on [ApiResult.ApiSuccess]
 *
 * @return [ApiResult] instance
 */
suspend fun <T : Any> ApiResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResult<T> = apply {
    if (this is ApiSuccess<T>) {
        executable(data)
    }
}

/**
 * Execute [executable] if the [ApiResult] is of type [ApiResult.ApiError]
 *
 * @param executable    Block to execute on [ApiResult.ApiError]
 *
 * @return [ApiResult] instance
 */
suspend fun <T : Any> ApiResult<T>.onError(
    executable: suspend (code: Int, message: String?) -> Unit
): ApiResult<T> = apply {
    if (this is ApiError<T>) {
        executable(code, message)
    }
}

/**
 * Execute [executable] if the [ApiResult] is of type [ApiResult.ApiException]
 *
 * @param executable    Block to execute on [ApiResult.ApiException]
 *
 * @return [ApiResult] instance
 */
suspend fun <T : Any> ApiResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): ApiResult<T> = apply {
    if (this is ApiException<T>) {
        executable(exception)
    }
}
