package {{ cookiecutter.package_dir }}.utils.extension

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CancellableContinuation

/**
 * Execute this function only if [CancellableContinuation.isActive] is true.
 * @param exec function to be executed
 */
fun <T> CancellableContinuation<T>.ifActive(exec: CancellableContinuation<T>.() -> Unit) {
    if (isActive) exec()
}

/**
 * Execute this function only if [CancellableContinuation.isCancelled] is true.
 * @param exec function to be executed
 */
fun <T> CancellableContinuation<T>.ifCancelled(exec: CancellableContinuation<T>.() -> Unit) {
    if (isCancelled) exec()
}

/**
 * Execute this function only if [CancellableContinuation.isCancelled] is true.
 * @param exec function to be executed
 */
fun <T> CancellableContinuation<T>.ifCompleted(exec: CancellableContinuation<T>.() -> Unit) {
    if (isCompleted) exec()
}

/**
 * Resume with [value] only when [CancellableContinuation.isActive] is true.
 * @param value value to be passed in resume
 */
fun <T> CancellableContinuation<T>.resumeIfActive(value: T) {
    ifActive {
        resume(value)
    }
}

/**
 * Resume with exception only when [CancellableContinuation.isActive] is true.
 * @param exception throwable to be passed in resume with exception
 */
fun <T> CancellableContinuation<T>.resumeWithExceptionIfActive(exception: Throwable) {
    ifActive {
        resumeWithException(exception)
    }
}
