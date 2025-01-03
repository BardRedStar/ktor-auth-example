package com.retroblade.achievo.di

import com.retroblade.achievo.routing.auth.AuthView
import com.retroblade.achievo.routing.auth.AuthViewImpl
import com.retroblade.achievo.routing.profile.ProfileView
import com.retroblade.achievo.routing.profile.ProfileViewImpl
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