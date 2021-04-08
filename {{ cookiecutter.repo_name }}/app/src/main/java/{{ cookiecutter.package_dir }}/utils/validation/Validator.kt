package {{ cookiecutter.package_dir }}.utils.validation

/**
 * Validators.
 */
object Validator {
    object PhoneNumber {
        const val MAX_LENGTH = 10
        const val MIN_LENGTH = 4
    }

    object CountryZipCode {
        const val MAX_LENGTH = 4 // Including '+' symbol
        const val MIN_LENGTH = 2 // Including '+' symbol
    }

    object Password {
        const val MIN_LENGTH = 8
        const val MAX_LENGTH = 12
    }

    /**
     * Is phone number and password valid.
     */
    fun isValid(countryZipCode: String, phoneNumber: String, password: String): Boolean =
        isValidCountryZipCode(countryZipCode) &&
            isValidPhoneNumber(phoneNumber) &&
            isValidPassword(password)

    /**
     * Is phone number valid.
     */
    fun isValidPhoneNumber(phoneNumber: String): Boolean =
        phoneNumber.isNotBlank() &&
            phoneNumber.length in PhoneNumber.MIN_LENGTH..PhoneNumber.MAX_LENGTH

    /**
     * Is password valid.
     */
    fun isValidPassword(password: String): Boolean =
        password.isNotBlank() &&
            password.length in Password.MIN_LENGTH..Password.MAX_LENGTH

    /**
     * Is country zip code valid.
     */
    fun isValidCountryZipCode(countryZipCode: String): Boolean =
        countryZipCode.isNotBlank() &&
            countryZipCode[0] == '+' &&
            countryZipCode.length in CountryZipCode.MIN_LENGTH..CountryZipCode.MAX_LENGTH &&
            countryZipCode.slice(1 until countryZipCode.length).matches("\\d+".toRegex()) // Exclude first character and check else are digits only

    /**
     * Is valid password and confirm password.
     */
    fun isValidPasswordAndConfirmPassword(password: String, confirmPassword: String): Boolean =
        (isValidPassword(password) && isValidPassword(confirmPassword)) &&
            password == confirmPassword
}
