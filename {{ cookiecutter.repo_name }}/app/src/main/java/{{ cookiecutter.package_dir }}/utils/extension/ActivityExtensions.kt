@file:Suppress("TooManyFunctions")

package {{ cookiecutter.package_name }}.utils.extension

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simformsolutions.app.R
import com.simformsolutions.app.utils.GOOGLE_MAP_PACKAGE
import com.simformsolutions.app.utils.ViewModelFactory
import com.simformsolutions.app.utils.ViewModelFactory

/**
 * Launch activity extension with optional bundles.
 * @receiver Context
 * @param options Bundle?
 * @param init Intent.() -> Unit
 */
inline fun <reified T : Any> Activity.launchActivity(options: Bundle? = null, noinline init: Intent.() -> Unit = {}) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

/**
 * Launch activity extension with optional bundles and finish current activity
 * @receiver Context
 * @param options Bundle?
 * @param init Intent.() -> Unit
 */
inline fun <reified T : Any> Activity.launchActivityAndFinish(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
    finish()
}

/**
 * @receiver Context
 * @param options Bundle?
 * @param init Intent.() -> Unit
 */
inline fun <reified T : Any> Activity.launchActivityWithResult(
    options: Bundle? = null,
    requestCode: Int,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

/**
 * Launch activity extension with optional bundles and finish current activity
 * @receiver Context
 * @param init Intent.() -> Unit
 */
inline fun <reified T : Any> Activity.launchActivityAndFinishAll(noinline init: Intent.() -> Unit = {}) {
    val intent = newIntent<T>(this)
    intent.init()
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}

/**
 * Launch activity extension with optional bundles and finish current activity
 * @receiver Context
 * @param data that you want to pass
 * @param init Intent.() -> Unit
 */
inline fun <reified T : Any> Activity.launchActivityWithData(
    data: Parcelable? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    intent.putExtra("data", data)
    startActivity(intent)
}

/**
 * Create T Type activity Intent
 * @param context Context
 * @return Intent
 */
inline fun <reified T : Any> newIntent(context: Context): Intent = Intent(context, T::class.java)

/**
 * @receiver MutableLiveData<T>
 * @param initialValue T
 * @return MutableLiveData<T>
 */
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

/**
 * This fun is used to hide keyboard.
 * @receiver AppCompatActivity
 */
fun Activity.hideKeyBoard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

/**
 * This function is used to start permission activity.
 * @receiver Activity
 */
fun Activity.startPermissionActivity() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

/**
 * This fun is used to open dialog fragment
 * @receiver AppCompatActivity
 * @param dialogFragment DialogFragment
 */
fun AppCompatActivity.showDialogFragment(dialogFragment: DialogFragment) {
    dialogFragment.show(supportFragmentManager, dialogFragment::class.java.simpleName)
}

/**
 * Create a ViewModel for the application when calling from an Activity
 *
 * @param   viewModelClass  The view model class we want to create
 * @return  The view model instance from [viewModelClass]
 */
fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

/**
 * Restarts an activity from itself with a fade animation
 * Keeps its existing extra bundles and has a intentBuilder to accept other parameters
 *
 * @param intentBuilder
 */
inline fun Activity.restart(intentBuilder: Intent.() -> Unit = {}) {
    val i = Intent(this, this::class.java)
    val oldExtras = intent.extras
    if (oldExtras != null)
        i.putExtras(oldExtras)
    i.intentBuilder()
    startActivity(i)
    finish()
}

/**
 * Add fragment with [layoutId] to the current [supportFragmentManager]
 *
 * @param fragment to add
 * @param tag of the fragment
 */
fun AppCompatActivity.addFragment(fragment: Fragment, @Nullable tag: String?, @IdRes layoutId: Int) {
    val fragmentTag = tag ?: fragment::class.java.canonicalName
    supportFragmentManager
        .beginTransaction()
        .add(layoutId, fragment, fragmentTag)
        .commit()
}

/**
 * Replace fragment with [layoutId] from the current [supportFragmentManager]
 *
 * @param title of the fragment
 * @param fragment to replace
 * @param tag of the fragment
 * @param layoutId of the fragment
 */
fun AppCompatActivity.replaceFragment(fragment: Fragment, @Nullable tag: String?, @IdRes layoutId: Int) {
    val fragmentTag = tag ?: fragment::class.java.canonicalName
    supportFragmentManager
        .beginTransaction()
        .replace(layoutId, fragment, fragmentTag)
        .commit()
}

/**
 * Removes the [fragment]
 */
fun AppCompatActivity.removeFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().remove(fragment).commit()
}

/**
 * Shows short length toast
 */
fun Activity.toast(resourceId: Int, length: Int = Toast.LENGTH_SHORT) {
    toast(getString(resourceId), length)
}

fun Activity.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

/**
 * Shows alert dialog
 *
 * @param title of dialog
 * @param message for dialog
 * @param positiveTitle for positive button
 * @param negativeTitle for negative button
 * @param positiveAction for positive button
 * @param negativeAction for negative button
 */
fun Activity.showDialog(title: String?, message: String?, positiveTitle: String = "Yes", negativeTitle: String = "No", positiveAction: () -> Unit, negativeAction: () -> Unit) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setIcon(android.R.drawable.ic_dialog_alert)

    builder.setPositiveButton(positiveTitle){dialogInterface, which ->
        positiveAction()
    }

    builder.setNegativeButton(negativeTitle){dialogInterface, which ->
        negativeAction()
    }

    val alertDialog: AlertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialog.show()
}

/**
 * Sets the screen brightness. Call this before setContentView.
 * 0 is dimmest, 1 is brightest. Default value is 1
 */
fun Activity.brightness(brightness: Float = 1f) {
    val params = window.attributes
    params.screenBrightness = brightness // range from 0 - 1 as per docs
    window.attributes = params
    window.addFlags(WindowManager.LayoutParams.FLAGS_CHANGED)
}

/**
 * Shows full screen dialog
 * @param layout xml file
 */
fun Activity.showFullScreenDialog(@LayoutRes layout: Int) {
    val dialog = Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
    dialog.setContentView(layout)
    dialog.show()
}

/**
 * Build notification
 *
 * @param message for notification
 * @param title of notification
 * @param notificationDrawable icon for notification
 */
fun Activity.buildNotification(message: String, title: String, @IdRes notificationDrawable: Int): Notification? {
    var notificationCompatBuilder: NotificationCompat.Builder? = null
    notificationCompatBuilder = NotificationCompat.Builder(this, createNotificationChannel(this))

    notificationCompatBuilder
        .setDefaults(Notification.DEFAULT_LIGHTS)
        .setSmallIcon(notificationDrawable)
        .setContentTitle(title)?.setContentText(message)
        ?.setPriority(NotificationCompat.PRIORITY_HIGH)
        ?.setAutoCancel(true)
        ?.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        ?.setColorized(true)
        ?.setOnlyAlertOnce(true)
    return notificationCompatBuilder.build()
}

private fun createNotificationChannel(context: Context): String {
    val channelName: String = "NotificationChannel"
    val channelID: String = "123"
    val channelDescription: String = "Channel for simple notification"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val name = channelName
        val description = channelDescription
        val notificationChannel = NotificationChannel(channelID, name, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.description = description
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.MAGENTA
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(notificationChannel)

    }
    return channelID
}

/**
 * Restart Application
 * */
fun Activity.restartApplication() {
    val packageManager: PackageManager = packageManager
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    if (intent == null) {
        restart()
        return
    }
    val componentName = intent.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    startActivity(mainIntent)
    overridePendingTransition(0, 0)
    Runtime.getRuntime().exit(0)
}

/**
 * Open URL in Browser
 *
 * @param urlString Web URL
 * @param exceptionMsg Custom Message for Exception
 * */
fun Activity.openURLInBrowser(
    urlString: String,
    exceptionMsg: String = getString(R.string.activity_not_found)
) {
    var url = urlString

    if (!url.startsWith("www.") && !url.startsWith("http")) {
        url = "http://$url"
    }

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        toast(exceptionMsg)
    }
}

/**
 * Open Address in Google Map
 *
 * @param address Address in String format
 * */
fun Activity.openAddressInGoogleMap(address: String) {
    val gmmIntentUri = Uri.parse("http://maps.google.co.in/maps?q=$address")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage(GOOGLE_MAP_PACKAGE)
    if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
    } else {
        openURLInBrowser("https://www.google.com/maps/place/$address/")
    }
}

/**
 * Open Dialer for Call
 *
 * @param number Phone Number
 * @param exceptionMsg Custom Message for Exception
 * */
fun Activity.openDialerForCall(
    number: String,
    exceptionMsg: String = getString(R.string.activity_not_found)
) {
    val intent = Intent(Intent.ACTION_DIAL)

    /* Send phone number to intent as data */
    intent.data = Uri.parse("tel:$number")

    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        toast(exceptionMsg)
    }
}

/**
 * Open Email
 *
 * @param email Email Address
 * @param exceptionMsg Custom Message for Exception
 * */
fun Activity.openEmail(
    email: String,
    exceptionMsg: String = getString(R.string.activity_not_found)
) {
    val emailIntent = Intent(Intent.ACTION_SENDTO)
    emailIntent.data = Uri.parse("mailto:$email")
    startActivity(emailIntent)

    try {
        startActivity(emailIntent)
    } catch (e: ActivityNotFoundException) {
        toast(exceptionMsg)
    }
}
