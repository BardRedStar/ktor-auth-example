package auth_sample.di

import auth_sample.routing.auth.AuthView
import auth_sample.routing.auth.AuthViewImpl
import auth_sample.routing.profile.ProfileView
import auth_sample.routing.profile.ProfileViewImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface ViewModule {
    @Binds
    @Reusable
    fun bindAuthView(impl: AuthViewImpl): AuthView

    @Binds
    @Reusable
    fun bindProfileView(impl: ProfileViewImpl): ProfileView
}