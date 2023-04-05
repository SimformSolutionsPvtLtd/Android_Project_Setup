package {{ cookiecutter.package_name }}

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import {{ cookiecutter.package_name }}.utils.NetworkUtil
import {{ cookiecutter.package_name }}.utils.ProductFlavor
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class {{ cookiecutter.app_name | replace(' ', '') }}App : Application() {

    private val validNetworks: MutableList<Network> = mutableListOf()

    override fun onCreate() {
        super.onCreate()

        initAppCenter()
        initTimber()
        observeNetwork()
    }

    private fun initAppCenter() {
        if (ProductFlavor.CURRENT == ProductFlavor.Flavor.DEV) {
            AppCenter.setLogLevel(Log.VERBOSE)
        }

        if (isFlavorProductionOrQA()) {
            AppCenter.start(
                this, BuildConfig.APPCENTER_SECRET,
                Analytics::class.java, Crashes::class.java
            )
        }
    }

    private fun isFlavorProductionOrQA(): Boolean = ProductFlavor.CURRENT == ProductFlavor.Flavor.QA ||
            ProductFlavor.CURRENT == ProductFlavor.Flavor.PRODUCTION

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            // Show logs only when on debug
            Timber.plant(Timber.DebugTree())
        }
    }

    /**
     * Observe network state.
     */
    private fun observeNetwork() {
        val connectivityManager: ConnectivityManager =
            getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                    val hasInternet =
                        networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            ?: false
                    if (hasInternet) {
                        validNetworks.add(network)
                        checkValidNetworks()
                    }
                }

                override fun onLost(network: Network) {
                    validNetworks.remove(network)
                    checkValidNetworks()
                }
            }
        )
    }

    private fun checkValidNetworks() {
        NetworkUtil.isNetworkConnected = validNetworks.isNotEmpty()
    }
}
