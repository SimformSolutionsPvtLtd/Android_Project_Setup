package {{ cookiecutter.package_name }}.utils.extension

import android.content.res.Resources
import com.simformsolutions.app.utils.DEG_RED
import java.lang.Math.PI

/**
 * Converts the angle in radians to degrees
 *
 * @return  The degree converted from radians
 */
fun Float.radToDeg(): Float = (this * (DEG_RED / PI)).toFloat()

/**
 * Converts the angle in degrees to radians
 *
 * @return  The radians converted from degrees
 */
fun Float.degToRad(): Float = ((PI * this) / DEG_RED).toFloat()

/**
 * Method to convert byteArray to String.
 */
fun ByteArray.byteArrayToString(): String {
    var des = ""
    var tmp: String
    for (i in this.indices) {
        tmp = Integer.toHexString(this[i].toInt() and HEX)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}