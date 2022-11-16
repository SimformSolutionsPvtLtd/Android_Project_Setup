package {{ cookiecutter.package_name }}.utils.extension

import android.content.res.Resources

/**
 * Convert dp to pixels
 *
 * @param   resources   A [Resources] instance
 * @return  Pixels converted from dp
 */
fun Int.dpToPx(resources: Resources): Int {
    return (this * resources.displayMetrics.density).toInt()
}

/**
 * Convert pixels to dp
 *
 * @param   resources   A [Resources] instance
 * @return  dp converted from pixels
 */
fun Int.pxToDp(resources: Resources): Int {
    return (this / resources.displayMetrics.density).toInt()
}
