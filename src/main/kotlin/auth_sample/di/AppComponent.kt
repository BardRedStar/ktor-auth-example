package auth_sample.di

import auth_sample.common.di.DIComponent
import auth_sample.common.di.FeatureComponentHolder
import auth_sample.routing.auth.AuthView
import auth_sample.routing.profile.ProfileView
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, ViewModule::class])
@Singleton
interface AppComponent: DIComponent {

    fun getAuthView(): AuthView

    fun getProfileView(): ProfileView

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }

}

object AppComponentHolder: FeatureComponentHolder<AppComponent>() {

    override fun build(): AppComponent {
        return DaggerAppComponent.factory().create()
    }
}