package {{ cookiecutter.package_name }}.utils

const val PHONE_REGEX = "^(\\+?\\d{1,4}[\\s-])?(?!0+\\s+,?\$)\\d{10}\\s*,?\$"
const val PHONE_REGEX_FORMAT = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*\$"
const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\\\\\/%§\"&“|`´}{°><:.;#')(@_\$\"!?*=^-]).{8,16}\$"
const val MOBILE_FORMAT_LENGTH = 14
const val PERMISSION_REQUEST_CODE = 1
const val DEG_RED = 180f
const val HEX = 0xFF
const val SUB = 3
const val ONE_DAY_MS = 86400000
const val MILLI = 172800000L
const val GOOGLE_MAP_PACKAGE = "com.google.android.apps.maps"
const val ACTIVITY_NOT_FOUND_EXCEPTION = "You may not have a proper app for viewing this content"