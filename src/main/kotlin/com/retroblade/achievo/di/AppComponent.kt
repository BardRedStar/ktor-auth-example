package com.retroblade.achievo.di

import com.retroblade.achievo.common.di.DIComponent
import com.retroblade.achievo.common.di.FeatureComponentHolder
import com.retroblade.achievo.data.service.TokenService
import com.retroblade.achievo.routing.auth.AuthView
import com.retroblade.achievo.routing.profile.ProfileView
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, ViewModule::class])
@Singleton
interface AppComponent: DIComponent {

    fun getAuthView(): AuthView

    fun getProfileView(): ProfileView

    fun getTokenService(): TokenService

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