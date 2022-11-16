package {{ cookiecutter.package_name }}.utils.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.simformsolutions.app.utils.result.Event
import com.simformsolutions.app.utils.result.EventObserver
import androidx.lifecycle.Observer

/**
 * LiveData extension to intercept each updates to the LiveData.
 * For example:
 * ```
 * private val mSomeLiveData = MutableLiveData<String>()
 * val someLiveData: LiveData<Int>
 *      get() = mSomeLiveData
 *          .onEach {
 *              // perform some operations on new data received from map function
 *          }
 * ```
 *
 * @param   onEach      A Lambda which provides updated value of calling LiveData
 *
 * @return  A [LiveData] of type [MediatorLiveData]
 */
inline fun <T> LiveData<T>.onEach(crossinline onEach: (T) -> Unit): LiveData<T> =
    MediatorLiveData<T>().apply {
        addSource(this@onEach) {
            value = it
            onEach(it)
        }
    }

/**
 * Extension function for observing [LiveData]
 * @param owner is [LifecycleOwner] which will be used to listen lifecycle changes
 * @param func is a function which will be executed whenever [LiveData] is changed
 */
fun <T> LiveData<T>.observe(owner: LifecycleOwner, func: (T) -> Unit) =
    observe(owner, Observer { value ->
        value?.let {
            func(it)
        }
    })

/**
 * Extension function for observing [LiveData] containing [Event]
 * @param owner is [LifecycleOwner] which will be used to listen lifecycle changes
 * @param func is a function which will be executed whenever [LiveData] is changed
 */
fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, func: (T) -> Unit) =
    observe(owner, EventObserver {
        func(it)
    })
