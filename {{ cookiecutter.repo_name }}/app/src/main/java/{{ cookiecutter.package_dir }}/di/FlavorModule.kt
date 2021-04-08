package {{ cookiecutter.package_dir }}.di

import {{ cookiecutter.package_dir }}.data.local.pref.FlavorPreferences
import {{ cookiecutter.package_dir }}.data.local.pref.FlavorPreferencesImpl
import {{ cookiecutter.package_dir }}.data.repository.FlavorRepository
import {{ cookiecutter.package_dir }}.data.repository.FlavorRepositoryImpl
import {{ cookiecutter.package_dir }}.ui.delegate.FlavorDelegate
import {{ cookiecutter.package_dir }}.ui.delegate.FlavorDelegateImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * Defines all the classes that need to be provided in the scope of the app.
 * If they are singleton mark them as '@Singleton'.
 *
 * NOTE : This module should only be used for app's flavor.
 */
@Module
@InstallIn(ApplicationComponent::class)
object FlavorModule {

    @Singleton
    @Provides
    fun provideFlavorPreferences(flavorPreferencesImpl: FlavorPreferencesImpl): FlavorPreferences = flavorPreferencesImpl

    @Singleton
    @Provides
    fun provideFlavorRepository(flavorRepositoryImpl: FlavorRepositoryImpl): FlavorRepository = flavorRepositoryImpl

    @Singleton
    @Provides
    fun provideFlavorDelegate(flavorDelegateImpl: FlavorDelegateImpl): FlavorDelegate = flavorDelegateImpl
}
