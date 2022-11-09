package {{ cookiecutter.package_name }}.di

import {{ cookiecutter.package_name }}.data.local.pref.FlavorPreferences
import {{ cookiecutter.package_name }}.data.local.pref.FlavorPreferencesImpl
import {{ cookiecutter.package_name }}.data.repository.FlavorRepository
import {{ cookiecutter.package_name }}.data.repository.FlavorRepositoryImpl
import {{ cookiecutter.package_name }}.utils.FlavorDelegate
import {{ cookiecutter.package_name }}.utils.FlavorDelegateImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

/**
 * Defines all the classes that need to be provided in the scope of the app.
 * If they are singleton mark them as '@Singleton'.
 *
 * NOTE : This module should only be used for app's flavor.
 */
@Module
@InstallIn(SingletonComponent::class)
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
