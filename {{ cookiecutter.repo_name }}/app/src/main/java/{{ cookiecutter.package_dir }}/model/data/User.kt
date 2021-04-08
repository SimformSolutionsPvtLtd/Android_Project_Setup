package {{ cookiecutter.package_dir }}.model.data

import com.google.gson.annotations.SerializedName

/**
 * User API response.
 */
data class User(
    @SerializedName("name")
    val name: Name = Name(),

    @SerializedName("location")
    val location: Location = Location(),

    @SerializedName("picture")
    val picture: Picture = Picture(),

    @SerializedName("login")
    val login: Login = Login()
) {
    data class Name(
        @SerializedName("title")
        val title: String = "",

        @SerializedName("first")
        val first: String = "",

        @SerializedName("last")
        val last: String = ""
    ) {
        override fun toString(): String = "$title $first $last"
    }

    data class Location (
        @SerializedName("street")
        val street: Street = Street(),

        @SerializedName("city")
        val city: String = "",

        @SerializedName("state")
        val state: String = "",

        @SerializedName("country")
        val country: String = "",

        @SerializedName("postcode")
        val postcode: String = "",

        @SerializedName("coordinates")
        val coordinates: Coordinates = Coordinates(),

        @SerializedName("timezone")
        val timezone: Timezone = Timezone()
    ) {
        fun address(): String {
            return "$street, $city, $postcode, $state, $country"
        }
    }

    data class Coordinates (
        @SerializedName("latitude")
        val latitude: String = "",

        @SerializedName("longitude")
        val longitude: String = ""
    )

    data class Timezone(
        @SerializedName("offset")
        val offset: String = "",

        @SerializedName("description")
        val description: String = ""
    )

    data class Street(
        @SerializedName("number")
        val number: Int = 0,

        @SerializedName("name")
        val name: String = ""
    ) {
        override fun toString(): String = "$number, $name"
    }

    data class Picture(
        @SerializedName("large")
        val large: String = "",

        @SerializedName("medium")
        val medium: String = "",

        @SerializedName("thumbnail")
        val thumbnail: String = ""
    )

    data class Login(
        @SerializedName("uuid")
        val uuid: String = ""
    )
}
