@file:Suppress("TooManyFunctions")

package {{ cookiecutter.package_name }}.utils.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.Uri
import android.provider.OpenableColumns
import android.provider.Settings
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Get file name of the file [Uri]
 * @param uri of the file
 * @return file name
 */
fun Context.getFileName(uri: Uri): String? =
    contentResolver.query(uri, null, null, null, null)
        ?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToNext()
            it.getString(nameIndex)
        }

/**
 * Extension function on [Context] to get the Drawable resources
 * @param id of the drawable resource
 * @return [Drawable]?
 */
fun Context.getResourceDrawable(@DrawableRes id: Int): Drawable? {
    return ResourcesCompat.getDrawable(this.resources, id, null)
}

/**
 * Extension function on [Context] to get the Color resources
 * @param id of the color resource
 * @return [Int]
 */
fun Context.getColorResource(@ColorRes id: Int): Int {
    return ResourcesCompat.getColor(this.resources, id, null)
}

/**
 * Extension function on [Context] to get the String resources
 *
 * @param id of the string resource
 * @return [String]
 */
fun Context.getStringResource(@StringRes id: Int): String {
    return this.resources.getString(id)
}

/**
 * Returns true if dark mode is ON
 */
val Context.isDarkTheme
    get() = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> false
        else -> false
    }

/**
 * Restart an application
 */
fun Context.restartApplication() {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    val componentName = intent?.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}

/**
 * Returns device id
 *
 * Drawback of using ANDROID_ID: Can change upon factory reset
 */
val Context.deviceID
    @SuppressLint("HardwareIds")
    get() = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

/**
 * Open device app settings to allow user to enable permissions
 */
fun Context.openSettings() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.data = Uri.fromParts("package", packageName, null)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

/**
 * Shows give layout file as a bottomsheet
 *
 * @param layout resource layout file
 */
fun Context.bottomSheet(@LayoutRes layout: Int, completion: (BottomSheetDialog) -> Unit) {
    val dialog = BottomSheetDialog(this)
    dialog.setContentView(layout)
    completion(dialog)
}

/**
 * Check is GPS Enabled
 * */
fun Context.isGPSEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    locationManager?.let {
        return locationManager.isProviderEnabled(LocationManager. GPS_PROVIDER)
    }
    return false
}