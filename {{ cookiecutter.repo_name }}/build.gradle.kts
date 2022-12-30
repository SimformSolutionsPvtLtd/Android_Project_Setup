import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(ClassPaths.ANDROID_GRADLE)
        classpath(ClassPaths.KOTLIN_GRADLE)
        classpath(ClassPaths.HILT_GRADLE)
    }
}

plugins {
    id(Plugins.SPOTLESS) version Versions.SPOTLESS
    id(Plugins.DETEKT) version Versions.DETEKT
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jitPack()
    }
}

subprojects {
    // TODO() : Remove when the Coroutine and Flow APIs leave experimental/internal/preview.
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs +=
            "-Xuse-experimental=" +
                    "kotlin.Experimental," +
                    "kotlinx.coroutines.ExperimentalCoroutinesApi," +
                    "kotlinx.coroutines.InternalCoroutinesApi," +
                    "kotlinx.coroutines.FlowPreview"
    }
}
