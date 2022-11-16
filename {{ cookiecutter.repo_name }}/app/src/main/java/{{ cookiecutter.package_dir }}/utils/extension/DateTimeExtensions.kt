package {{ cookiecutter.package_name }}.utils.extension

import android.content.Context
import android.text.format.DateFormat
import com.simformsolutions.app.utils.ONE_DAY_MS
import java.text.SimpleDateFormat
import java.util.*

/**
* get Current Date.
*/
val currentDate get() = Date(System.currentTimeMillis())

/**
 * Gives [Calendar] object from Date
 */
inline val Date.calendar: Calendar
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar
    }

/**
 * Gets value of Milliseconds of current time
 */
inline val now: Long
    get() = Calendar.getInstance().timeInMillis

/**
 * Formats date according to device's default date format
 *
 * @param date
 * @return date according to divice's default format
 */
fun Context.formatDateAccordingToDevice(date: Date): String {
    val format = DateFormat.getDateFormat(this)
    return format.format(date)
}

/**
 * Formats time according to device's default time format
 *
 * @param time
 * @return time according to divice's default format
 */
fun Context.formatTimeAccordingToDevice(date: Date): String {
    val format = DateFormat.getTimeFormat(this)
    return format.format(date)
}

/**
 * Convert milli Seconds to date
 *
 * @param format for the date
 */
fun Long.milliSecondsToStringDate(format: String): String {
    val simpleDateFormat = SimpleDateFormat(format)
    val dateString = simpleDateFormat.format(this)
    return String.format(dateString)
}

/**
 * Convert a given date to milliseconds
 */
fun Date.toMillis() : Long {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.timeInMillis
}

/**
 * Adds days to given date
 * @param days you want to add
 */
fun Date.plusDays(days: Int): Date {
    return Date(this.time + (days * ONE_DAY_MS))
}

/**
 * Adds milliseconds to given date
 * @param days you want to add
 */
fun Date.plusMillis(millis: Long): Date {
    return Date(this.time + millis)
}