package {{ cookiecutter.package_name }}.data.remote.request

import com.google.gson.annotations.SerializedName

// TODO : Remove this when real request models available
data class DummyRequest(
    @SerializedName("name")
    val name: String = "",

    @SerializedName("location")
    val location: String = ""
)
