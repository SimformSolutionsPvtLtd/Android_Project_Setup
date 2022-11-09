package {{ cookiecutter.package_name }}.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("results")
    val results: List<User>,

    @SerializedName("info")
    val info: Info
) {
    data class Info(
        @SerializedName("seed")
        val seed: String,

        @SerializedName("results")
        val results: Int,

        @SerializedName("page")
        val page: Int,

        @SerializedName("version")
        val version: String
    )
}
