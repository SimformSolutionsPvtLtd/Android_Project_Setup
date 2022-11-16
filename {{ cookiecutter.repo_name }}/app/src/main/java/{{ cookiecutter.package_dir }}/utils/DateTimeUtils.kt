package com.simformsolutions.app.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Gets current time in given format
 *
 * @param stringFormat format of the date
 * @return date in specified format
 */
fun getCurrentTimeInFormat(time: String): String {
    val currentTime = Date()
    return SimpleDateFormat(time, Locale.getDefault()).format(currentTime)
}