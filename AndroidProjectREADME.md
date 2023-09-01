
# Project Name

[![Kotlin Version](https://img.shields.io/badge/Kotlin-v1.3.51-blue.svg)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a1c9a1b1d1ce4ca7a201ab93492bf6e0)](https://www.codacy.com/app/LDRAlighieri/Corbind?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=LDRAlighieri/Corbind&amp;utm_campaign=Badge_Grade)
[![Build Status](https://app.bitrise.io/app/618cab59176816b2/status.svg?token=gcdmjYaFxQSBs5BBtJ-DkQ&branch=develop)](https://app.bitrise.io/app/618cab59176816b2)


### [Check it out on PlayStore](https://play.google.com/store/apps/details?id=com.grubmaster)

Getting Started
------------------------
Add some sort of description about the project. These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

Prerequisites
------------------------

### Android Studio IDE setup

For development, it requires `Android studio version 3.5` or above. The latest version can be downloaded from [here](https://developer.android.com/studio/).

### Libraries Used

* [Foundation][0] - Components for core system capabilities, Kotlin extensions, and support for multidex and automated testing.
  * [AppCompat][1] - Degrade gracefully on older versions of Android.
  * [Android KTX][2] - Write more concise, idiomatic Kotlin code.
  * [Test][4] - An Android testing framework for unit and runtime UI tests.
* [Architecture][10] - A collection of libraries that help you design robust, testable, and maintainable apps. Start with classes for managing your UI component lifecycle and handling data persistence.
  * [Data Binding][11] - Declaratively bind observable data to UI elements.
  * [Lifecycles][12] - Create a UI that automatically responds to lifecycle events.
  * [LiveData][13] - Build data objects that notify views when the underlying database changes.
  * [Navigation][14] - Handle everything needed for in-app navigation.
  * [Room][16] - Access your app's SQLite database with in-app objects and compile-time checks.
  * [ViewModel][17] - Store UI-related data that isn't destroyed on app rotations. Easily schedule asynchronous tasks for optimal execution.
  * [WorkManager][18] - Manage your Android background jobs.
  * [Espresso][19] - Use Espresso to write concise, beautiful, and reliable Android UI tests.
* [UI][30] - Details on why and how to use UI Components in your apps - together or separate
  * [Animations & Transitions][31] - Move widgets and transition between screens.
  * [Fragment][34] - A basic unit of composable UI.
  * [Layout][35] - Lay out widgets using different algorithms.
* Third party
  * [Kotlin Coroutines][91] for managing background threads with simplified code and reducing needs for callbacks
  * [Retrofit][92] A type-safe HTTP client for Android and Java.
  * [Glide][93] An image loading and caching library for Android focused on smooth scrolling.
  * [SDP][94] An Android SDK that provides a new size unit - sdp (scalable dp). This size unit scales with the screen size.
  * [SSP][95] Variant of sdp project based on the sp size unit.
  * [Timber][96] A logger with a small, extensible API which provides utility on top of Android's normal Log class.

[0]: https://developer.android.com/jetpack/components
[1]: https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat
[2]: https://developer.android.com/kotlin/ktx
[4]: https://developer.android.com/training/testing/
[10]: https://developer.android.com/jetpack/arch/
[11]: https://developer.android.com/topic/libraries/data-binding/
[12]: https://developer.android.com/topic/libraries/architecture/lifecycle
[13]: https://developer.android.com/topic/libraries/architecture/livedata
[14]: https://developer.android.com/topic/libraries/architecture/navigation/
[16]: https://developer.android.com/topic/libraries/architecture/room
[17]: https://developer.android.com/topic/libraries/architecture/viewmodel
[18]: https://developer.android.com/topic/libraries/architecture/workmanager
[19]: https://developer.android.com/training/testing/espresso
[30]: https://developer.android.com/guide/topics/ui
[31]: https://developer.android.com/training/animation/
[34]: https://developer.android.com/guide/components/fragments
[35]: https://developer.android.com/guide/topics/ui/declaring-layout
[91]: https://kotlinlang.org/docs/reference/coroutines-overview.html
[92]: https://square.github.io/retrofit/
[93]: https://bumptech.github.io/glide/
[94]: https://github.com/intuit/sdp
[95]: https://github.com/intuit/ssp
[96]: https://github.com/JakeWharton/timber

### Architecture used
- MVVM architecture, ViewModel and LiveData
- [Koin](https://insert-koin.io/): A pragmatic lightweight dependency injection framework for Kotlin developers.

### Static Code Analysis Tool
In this project, we have used eight different type of Static Code Analysis tools as listed below

    1.  CheckStyle
    2.  Lint
    3.  Findbugs
    4.  PMD
    5.  KtLint
    6.  Detekt
    7.  Spotless
    8.  Sonarqube

### Permissions used

##### Automatically granted
* `INTERNET`: requires for accessing the internet.
* `ACCESS_NETWORK_STATE`: used by the browser to stop loading resources when network access is lost.

##### Requested only when needed
* `WRITE_EXTERNAL_STORAGE`: Requires for downloading and storing files.
* `READ_EXTERNAL_STORAGE`: Requires for reading files from the external storage.
* `ACCESS_FINE_LOCATION`: Requires for sites like Google Maps, requires "Location access" option to be enabled (default disabled).
* `RECORD_AUDIO`: Requires for supporting Audio recording option to be enabled (by default, its disabled).
* `CAMERA`: Requires for supporting Camera image capture option to be enabled (by default, it’s disabled).

## How to setup project?

1. Clone this repository in a location of your choice, like your projects folder, using this command  `"git clone <your-repo-url>"` over terminal.

2. Start Android Studio and go File/Open select project folder.

3. It will take some time to build and download Gradle dependencies.

4. Once completed, there will be a message that says `"BUILD SUCCESSFUL"`.

5. Yup! You are all set now. To run just hit ► the (run) button.  🚀

### How to use?

There are two different `productflavors` available for running the project either in `development` or in `production` environment.

1. **Development** - Set the active Build Variant as `"developmentDebug"` to run the project in the Development environment.
2. **Production** - Set the active Build Variant as `"productionRelease"` to run the project in the Production environment.

```
  productFlavors {
            development {
                it.buildConfigField 'String', 'BSA_BASE_URL', '"<stage-url>"'
                it.buildConfigField 'Boolean', "CRASHLYTICS_ENABLED", 'true'
            }
            production {
                it.buildConfigField 'String', 'BSA_BASE_URL', '"<production-url>"'
                it.buildConfigField 'Boolean', "CRASHLYTICS_ENABLED", 'false'
            }
        }
```

## Beta Testing

We are using [Fabric Beta](https://docs.fabric.io/apple/beta/overview.html#) for downloading the app for beta testing.

To checkout the beta versions of the application using Fabric, you’ll need to receive an invitation email from the developer or get access to a public invitation link, and have a device that you can use to test.


## Important credentials

- **Codacy**: developer@simformsolutions.com
- **Bitrise**: developer@simformsolutions.com
- **Firebase**:  developer@simformsolutions.com
- **Fabric**:  developer@simformsolutions.com
- **Facebook**:   rcbrcb13@gmail.com