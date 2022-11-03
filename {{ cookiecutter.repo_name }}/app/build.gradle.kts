plugins {
    id(Plugins.ANDROID_APPLICATION)
    kotlin(Plugins.Kotlin.ANDROID)
    kotlin(Plugins.Kotlin.KAPT)
    id(Plugins.HILT)
}

apply {
    from(rootPath(appendPath = Plugins.QUALITY))
}

android {
    compileSdk = Versions.COMPILE_SDK
    buildToolsVersion = Versions.BUILD_TOOLS

    signingConfigs {
        create("release") {
            storeFile = rootProject.file(project.property("releaseKeystoreFileName") as String)
            storePassword = project.property("releaseStorePassword") as String
            keyAlias = project.property("releaseKeyAlias") as String
            keyPassword = project.property("releaseKeyPassword") as String
        }
    }

    defaultConfig {
        applicationId = App.ID
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = App.Version.CODE
        versionName = App.Version.NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")
        }

        buildTypes.forEach { type ->
            val appCenter = BuildConfigFields.APPCENTER_SECRET
            type.buildConfigField(appCenter.type, appCenter.title, appCenter.value)
        }
    }


    flavorDimensions(App.Dimension.DEFAULT)

    productFlavors {
        create(App.Flavor.DEV) {

        }

        create(App.Flavor.QA) {

        }

        create(App.Flavor.PRODUCTION) {

        }
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation(defaultFileTree())

    // Core
    implementation(Libs.CORE_KTX)

    // UI
    implementation(Libs.APPCOMPAT)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.RECYCLERVIEW)

    // Jetpack
    implementation(Libs.ACTIVITY_KTX)
    implementation(Libs.FRAGMENT_KTX)
    // ViewModel
    implementation(Libs.LIFECYCLE_VIEWMODEL_KTX)
    // LiveData
    implementation(Libs.LIFECYCLE_LIVEDATA_KTX)
    // Navigation
    implementation(Libs.NAVIGATION_UI_KTX)
    implementation(Libs.NAVIGATION_FRAGMENT_KTX)
    {%- if cookiecutter.include_room_db == 'y' %}
    // Room
    implementation(Libs.ROOM_RUNTIME)
    implementation(Libs.ROOM_KTX)
    kapt(Libs.ROOM_COMPILER)
    {% endif %}
    // Hilt
    implementation(Libs.HILT)
    kapt(Libs.HILT_DAGGER_COMPILER)
    kapt(Libs.HILT_COMPILER)

    // Material
    implementation(Libs.MATERIAL)

    // Coroutines
    implementation(Libs.COROUTINES_CORE)
    implementation(Libs.COROUTINES_ANDROID)

    // Retrofit
    implementation(Libs.RETROFIT)
    implementation(Libs.RETROFIT_GSON)
    implementation(Libs.OKHTTP3)
    implementation(Libs.OKHTTP3_LOGGING_INTERCEPTOR)

    // Gson
    implementation(Libs.GSON)

    // AppCenter
    implementation(Libs.APPCENTER_ANALYTICS)
    implementation(Libs.APPCENTER_CRASHES)

    // Timber
    implementation(Libs.TIMBER)

    // Easy permissions
    implementation(Libs.EASY_PERMISSIONS)

    // Kotlin Reflect
    implementation(kotlin(Libs.REFLECT))

    // Alerter
    implementation(Libs.ALERTER)

    // Loading Button
    implementation(Libs.LOADING_BUTTON)

    // Shimmer
    implementation(Libs.SHIMMER)
    
    // Glide
    implementation(Libs.GLIDE)
    {%- if cookiecutter.include_testing == 'y' %}
    // Unit testing
    testImplementation(Libs.JUNIT)
    testImplementation(Libs.JUNIT_EXT)
    testImplementation(Libs.ARCH_CORE_TESTING)
    testImplementation(Libs.COROUTINES_TEST)
    testImplementation(Libs.HAMCREST)
    testImplementation(Libs.MOCKITO_CORE)
    testImplementation(Libs.MOCKITO_KOTLIN)

    // UI testing
    androidTestImplementation(Libs.TEST_RUNNER)
    androidTestImplementation(Libs.JUNIT_EXT)
    androidTestImplementation(Libs.TEST_RULES)
    androidTestImplementation(Libs.ESPRESSO_CORE)
    androidTestImplementation(Libs.ESPRESSO_CONTRIB)
    {% endif %}
}
