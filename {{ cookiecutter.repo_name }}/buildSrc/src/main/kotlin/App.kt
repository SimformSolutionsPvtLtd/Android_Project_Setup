object App {
    const val ID = "{{ cookiecutter.package_name }}"

    object Version {
        const val CODE = 1
        const val NAME = "1.0"
    }

    object Dimension {
        const val DEFAULT = "default"
    }

    object Flavor {
        const val DEV = "dev"
        const val QA = "qa"
        const val PRODUCTION = "production"
    }
}
