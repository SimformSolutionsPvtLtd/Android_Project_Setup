@file:Suppress("TooManyFunctions")

package {{ cookiecutter.package_name }}.utils.extension

import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simformsolutions.app.utils.ViewModelFactory

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

/**
 * Create a ViewModel for the application when calling from a Fragment
 *
 * @param   viewModelClass  The view model class we want to create
 * @return  The view model instance from [viewModelClass]
 */
fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

/**
 * Add fragment
 *
 * @param fragment to add
 * @param tag of the fragment
 * @param layoutId of the fragment
 */
fun Fragment.addFragment(fragment: Fragment, @Nullable tag: String?, @IdRes layoutId: Int) {
    val fragmentTag = tag ?: fragment::class.java.canonicalName
    val activity = this.requireContext() as AppCompatActivity
    activity.supportFragmentManager
        .beginTransaction()
        .add(layoutId, fragment, fragmentTag)
        .commit()
}

/**
 * Replace fragment with [layoutId] from the current [supportFragmentManager]
 * when [title] is required
 *
 * @param fragment to replace
 * @param tag of the fragment
 * @param layoutId of the fragment
 */
fun Fragment.replaceFragment(fragment: Fragment, @Nullable tag: String?, @IdRes layoutId: Int) {
    val activity = this.requireContext() as AppCompatActivity
    val fragmentTag = tag ?: fragment::class.java.canonicalName

    activity.supportFragmentManager
        .beginTransaction()
        .replace(layoutId, fragment, fragmentTag)
        .commit()
}

/**
 * Removes the [fragment]
 */
fun Fragment.removeFragment() {
    if (isAdded) {
        val activity = this.requireContext() as AppCompatActivity
        activity.supportFragmentManager.removeFragmentWithStateLoss(this)
    }
}

fun FragmentManager.removeFragmentWithStateLoss(fragment: Fragment) {
    beginTransaction().remove(fragment).commitNowAllowingStateLoss()
}

/**
 * Show fragment
 *
 * @param showFragment fragment you want to show
 */
fun FragmentManager.show(showFragment: Fragment) {
    val ft = this.beginTransaction()
    ft.show(showFragment)
    ft.commit()
}

/**
 * Remove fragment
 *
 * @param removeFragment fragment you want to remove
 */
fun FragmentManager.remove(vararg removeFragment: Fragment) {
    val ft = this.beginTransaction()
    for (fragment in removeFragment) {
        ft.remove(fragment)
    }
    ft.commit()
}
