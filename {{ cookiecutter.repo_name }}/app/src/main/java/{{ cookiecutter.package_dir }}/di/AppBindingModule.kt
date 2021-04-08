package {{ cookiecutter.package_dir }}.di

import {{ cookiecutter.package_dir }}.data.repository.UserRepository
import {{ cookiecutter.package_dir }}.data.repository.UserRepositoryImpl
import {{ cookiecutter.package_dir }}.utils.ResourceHelper
import {{ cookiecutter.package_dir }}.utils.ResourceHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * Defines all the classes that need to be provided in the scope of the app.
 * If they are singleton mark them as '@Singleton'.
 */
@Module
@InstallIn(ApplicationComponent::class)
abstract class AppBindingModule {

    @Singleton
    @Binds
    abstract fun bindResourceHelper(impl: ResourceHelperImpl): ResourceHelper

    @Singleton
    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
