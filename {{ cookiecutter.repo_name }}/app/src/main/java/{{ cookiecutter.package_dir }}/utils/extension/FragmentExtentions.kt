package {{ cookiecutter.package_dir }}.utils.extension

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

/**
 * This fun is used to open dialog fragment
 * @receiver Fragment
 * @param dialogFragment DialogFragment
 */
fun Fragment.showDialogFragment(dialogFragment: DialogFragment, init: Bundle.() -> Unit = {}) {
    val bundle = Bundle()
    bundle.init()
    dialogFragment.arguments = bundle
    dialogFragment.show(parentFragmentManager, dialogFragment::class.java.simpleName)
}

/**
 * @receiver Context
 * @param options Bundle?
 * @param init Intent.() -> Unit
 */
inline fun <reified T : Any> Fragment.launchActivityWithResult(
    options: Bundle? = null,
    requestCode: Int,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(requireContext())
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

/**
 * Launch activity extension with optional bundles.
 * @receiver Context
 * @param options Bundle?
 * @param init Intent.() -> Unit
 */
inline fun <reified T : Any> Fragment.launchActivity(options: Bundle? = null, noinline init: Intent.() -> Unit = {}) {
    val intent = newIntent<T>(requireActivity())
    intent.init()
    startActivity(intent, options)
}
