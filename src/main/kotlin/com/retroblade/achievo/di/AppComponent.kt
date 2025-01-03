package com.retroblade.achievo.di

import com.retroblade.achievo.common.di.DIComponent
import com.retroblade.achievo.common.di.FeatureComponentHolder
import com.retroblade.achievo.utils.TokenUtils
import com.retroblade.achievo.routing.auth.AuthController
import com.retroblade.achievo.routing.profile.ProfileController
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent: DIComponent {

    fun getAuthController(): AuthController

    fun getProfileController(): ProfileController

    fun getTokenUtils(): TokenUtils

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