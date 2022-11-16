@file:Suppress("TooManyFunctions")

package {{ cookiecutter.package_name }}.utils.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.Base64
import android.util.Patterns
import android.widget.Toast
import com.simformsolutions.app.utils.CREDIT_CARD_SEPARATOR
import com.simformsolutions.app.utils.MOBILE_FORMAT_LENGTH
import com.simformsolutions.app.utils.PASSWORD_PATTERN
import com.simformsolutions.app.utils.PHONE_REGEX
import com.simformsolutions.app.utils.PHONE_REGEX_FORMAT
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Checks given string is contain at least char and length
 * @receiver String
 * @return Boolean
 */
fun String.isValidPasswordLength(): Boolean {
    return (Pattern.compile(PASSWORD_PATTERN).matcher(this).matches())
}

/**
 * Checks email is valid or not
 * @receiver String
 * @return Boolean
 */
fun String.isValidEmail(): Boolean = isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Verifies phone number
 * @receiver String
 * @return
 */
fun String.verifyPhone(): Boolean {
    return !TextUtils.isEmpty(this) && this.length >= MOBILE_FORMAT_LENGTH && Pattern.compile(PHONE_REGEX_FORMAT).matcher(this).matches()
}

/**
 * Checks phone number is valid or not
 * @receiver String
 * @return
 */
fun String.isValidPhone(): Boolean {
    return Pattern.compile(PHONE_REGEX).matcher(this).matches()
}

/**
 * Checks web url is valid or not
 * @receiver String
 * @return Boolean
 */
fun String.verifyWebUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()

/**
 * Remove all white spaces
 */
fun String.removeWhitespaces() = replace(" ", "")

/**
 * Checks string is alphanumeric or not
 */
val String.isAlphanumeric: Boolean
    get() = matches(Regex("[A-Za-z0-9]*"))

/**
 * Checks string is integer number or not
 */
val String.isIntegerNumber: Boolean
    get() = toIntOrNull() != null

/**
 * Checks string Double number or not
 */
val String.toDecimalNumber: Boolean
    get() = toDoubleOrNull() != null

/**
 * Converts string to JSON object
 */
val String.jsonObject: JSONObject?
    get() = try {
        JSONObject(this)
    } catch (e: JSONException) {
        null
    }

/**
 * Converts string to JSON Array
 */
val String.jsonArray: JSONArray?
    get() = try {
        JSONArray(this)
    } catch (e: JSONException) {
        null
    }

/**
 * string to date converter
 *
 * @param withFormat format for the date
 */
fun String.toDate(withFormat: String = "yyyy/MM/dd hh:mm"): Date {
    val dateFormat = SimpleDateFormat(withFormat)
    var convertedDate = Date()
    try {
        convertedDate = dateFormat.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return convertedDate
}

/**
 * Encode String to URL
 */
fun String.encodeToUrl(charSet: String = "UTF-8"): String = URLEncoder.encode(this, charSet)

/**
 * Decode String to URL
 */
fun String.decodeToUrl(charSet: String = "UTF-8"): String = URLDecoder.decode(this, charSet)

/**
 * Returns true if this string contains exactly the provided string.
 * This method uses RegEx to evaluate and its case-sensitive. What makes it different from the classic
 * [contains] is that it doesn't uses [indexOf], hence it's more performant when used on long char sequences
 * and has much higher probabilities of not returning false positives per approximation.
 */
fun String.containsExact(string: String): Boolean =
    Pattern.compile("(?<=^|[^a-zA-Z0-9])\\Q$string\\E(?=\$|[^a-zA-Z0-9])")
        .matcher(this)
        .find()

/**
 * Save String to a Given File
 */
fun String.saveToFile(file: File) = FileOutputStream(file).bufferedWriter().use {
    it.write(this)
    it.flush()
    it.close()
}

/**
 * Checks string is empty or null
 */
inline fun String.ifIsNullOrEmpty(action: () -> Unit) {
    if (isNullOrEmpty()) action()
}

/**
 * Checks string is not empty or not null
 */
inline fun String.ifIsNotNullOrEmpty(action: () -> Unit) {
    if (!isNullOrEmpty()) action()
}

/**
 * Removes all occurrences of the [value] in the string
 * @param value A vlaue
 * @param ignoreCase Ignore case?
 * @return A new string with all occurrences of the [value] removed
 */
fun String.remove(value: String, ignoreCase: Boolean = false): String = replace(value, "", ignoreCase)

/**
 * Captialize firts letter of the string
 */
fun String.capitalizeFirstLetter(): String {
    return this.substring(0, 1).uppercase() + this.substring(1)
}

/**
 * Captialize firts letter of the each word in the string
 */
fun String.capitalizeFirstLetterEachWord(): String {
    return this.lowercase()
        .split(" ")
        .joinToString(" ") { it.capitalize() }
}

/**
 * Get md5 string.
 */
val String.md5 get() = this.hashAString("MD5", toByteArray())

/**
 * Get sha1 string.
 */
val String.sha1 get() = this.hashAString("SHA-1", toByteArray())


fun String.removeSpace(): String = replace(" ", "")

fun String.showAsToast(context: Context, duration: Int = 2) {
    Toast.makeText(context, this, duration).show()
}

/**
 * Computes the substring starting at start and ending at endInclusive,
 *      which is the length of text unless specified otherwise.
 */
fun String.stringSubstring(start: Int, endInclusive: Int = this.length - 1): String {
    var result = ""
    val startFixed = if (start < 0) 0 else start
    val endFixed = if (endInclusive > this.length - 1) this.length - 1 else endInclusive

    for (i in startFixed..endFixed) {
        result += this[i]
        if (i + 1 >= this.length) return result
    }
    return result
}

/**
 * Method to get encrypted string.
 */
@Throws(NoSuchAlgorithmException::class)
private fun String.hashAString(type: String, salt: ByteArray): String? {
    if (this.isEmpty()) {
        return null
    }
    val messageDigest: MessageDigest = MessageDigest.getInstance(type)
    val bytes = messageDigest.digest(salt)
    return bytes.byteArrayToString()
}

/**
 **
 * Encrypt String to AES with the specific Key
 */
@Throws(InvalidKeyException::class, UnsupportedEncodingException::class, InvalidKeySpecException::class)
fun String.encryptAES(key: String): String {
    var crypted: ByteArray? = null
    val skey = SecretKeySpec(key.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, skey)
    crypted = cipher.doFinal(toByteArray())
    return String(Base64.encode(crypted, Base64.DEFAULT))
}

/**
 * Decrypt String to AES with the specific Key
 */
@Throws(InvalidKeyException::class, InvalidKeySpecException::class)
fun String.decryptAES(key: String): String? {
    var output: ByteArray? = null
    val skey = SecretKeySpec(key.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, skey)
    output = cipher.doFinal(Base64.decode(this, Base64.DEFAULT))
    return output?.let { String(it) }
}

/**
 * Method to convert one date format to another format
 *
 * @receiver String date in any date format like dd-mm-yyyy
 * @param defaultFormat provide default String format in which you are passing your date
 * @param formatWanted  provide string format in which you want it.
 * @return String formatted String date according to your provided format,
 * in case of parse error it will send default string date provided
 */
@Throws(ParseException::class)
fun String.convertDate(defaultFormat: String, formatWanted: String): String {
    val format1 = SimpleDateFormat(defaultFormat, Locale.getDefault())
    val format2 = SimpleDateFormat(formatWanted, Locale.getDefault())
    return format2.format(format1.parse(this) ?: this)
}

/**
 * Copy text to clipboard
 *
 * @receiver context
 * @param textToCopy
 */
fun String.copyTextToClipboard(context: Context) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", this)
    clipboardManager.setPrimaryClip(clipData)
}

/**
 * Convert Hex string to android color
 */
val String.asColor: Int?
    get() = try {
        Color.parseColor(this)
    } catch (e: java.lang.IllegalArgumentException) {
        null
    }

/**
 * Convert given number into credit card format
 */
val String.creditCardFormatted: String
    get() {
        val preparedString = replace(" ", "").trim()
        val result = StringBuilder()
        for (i in preparedString.indices) {
            if (i % 4 == 0 && i != 0) {
                result.append(" ")
            }
            result.append(preparedString[i])
        }
        return result.toString()
    }

/**
 * hex to RGB converter
 *
 * @receiver hext color string
 * @return RGB color in string
 */
fun String.hextoRGB() : Triple<String, String, String>{
    var name = this
    if (!name.startsWith("#")){
        name = "#$this"
    }
    val color = Color.parseColor(name)
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)

    return Triple(red.toString(), green.toString(), blue.toString())
}

/**
 * Convert string to camel case
 */
val String.camelCased: String
    get() {
        val split = lowercase(Locale.getDefault()).split(' ', '\n', '\t').toMutableList()
        if (split.size > 1) {
            for (i in 1..split.size - 1) {
                if (split[i].length > 1) {
                    val charArray = split[i].toCharArray()
                    charArray[0] = charArray[0].toUpperCase()
                    split[i] = String(charArray)
                }
            }
        }
        return split.joinToString("")
    }
