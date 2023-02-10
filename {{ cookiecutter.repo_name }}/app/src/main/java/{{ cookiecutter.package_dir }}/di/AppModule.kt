package {{ cookiecutter.package_name }}.di

{% if cookiecutter.include_room_db == 'y' %}import android.content.Context
import {{ cookiecutter.package_name }}.data.local.db.AppDatabase
{% endif -%}import dagger.Module
import dagger.hilt.components.SingletonComponent
{%- if cookiecutter.include_room_db == 'y' %}
import dagger.Provides{% endif %}
import dagger.hilt.InstallIn
{%- if cookiecutter.include_room_db == 'y' %}
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton{% endif %}

/**
 * Defines all the classes that need to be provided in the scope of the app.
 * If they are singleton mark them as '@Singleton'.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {% if cookiecutter.include_room_db == 'y' %}{

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.buildDatabase(context)
}
{% endif %}