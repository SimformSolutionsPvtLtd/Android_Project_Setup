package {{ cookiecutter.package_dir }}.utils

object ProductFlavor {
    val CURRENT: Flavor = Flavor.PRODUCTION

    sealed class Flavor(val name: String) {
        object DEV : Flavor("DEV")

        object QA : Flavor("QA")

        object PRODUCTION : Flavor("PRODUCTION")

        override fun toString(): String = name
    }
}
