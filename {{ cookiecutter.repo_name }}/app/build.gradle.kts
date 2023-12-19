plugins {
    alias (libs.plugins.android.application)
    alias (libs.plugins.kotlin.android)
    alias (libs.plugins.kotlin.kapt)
    alias (libs.plugins.hilt.plugin)
}

apply {
    from(rootPath(appendPath = Plugins.QUALITY))
}

android {
    namespace = App.ID
    compileSdk = libs.versions.compile.sdk.version.get().toInt()

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
        minSdk = libs.versions.min.sdk.version.get().toInt()
        targetSdk = libs.versions.compile.sdk.version.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        {%- if cookiecutter.include_room_db == 'y' %}

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }{% endif %}
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


    flavorDimensions += App.Dimension.DEFAULT

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
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(defaultFileTree())

    // Core
    implementation(libs.core.ktx)

    // UI
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)

    // Jetpack
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)

    // ViewModel
    implementation(libs.lifecycle.viewmodel.ktx)

    // LiveData
    implementation(libs.lifecycle.livedata.ktx)

    // Navigation
    implementation(libs.navigation.ui.ktx)
    implementation(libs.navigation.fragment.ktx)

    {%- if cookiecutter.include_room_db == 'y' %}
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
    {% endif %}

    // Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.dagger.compiler)
    kapt(libs.hilt.compiler)

    // Material
    implementation(libs.material)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)

    // Gson
    implementation(libs.gson)

    // AppCenter
    implementation(libs.appcenter.analytics)
    implementation(libs.appcenter.crashes)

    // Timber
    implementation(libs.timber)

    // Kotlin Reflect
    implementation(libs.kotlin.reflect)

    // Alerter
    implementation(libs.alerter)

    // Shimmer
    implementation(libs.shimmer)

    // Glide
    implementation(libs.glide)

    {%- if cookiecutter.include_testing == 'y' %}
    // Unit testing
    testImplementation(libs.junit)
    testImplementation(libs.junit.ext)
    testImplementation(libs.arch.core.testing)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.hamcrest)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    // UI testing
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.test.rules)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.contrib)
    {% endif %}
}
