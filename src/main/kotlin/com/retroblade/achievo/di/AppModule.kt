package com.retroblade.achievo.di

import com.retroblade.achievo.repository.*
import com.retroblade.achievo.utils.TokenUtils
import com.retroblade.achievo.utils.TokenUtilsImpl
import com.retroblade.achievo.service.AuthService
import com.retroblade.achievo.service.AuthServiceImpl
import com.retroblade.achievo.service.ProfileService
import com.retroblade.achievo.service.ProfileServiceImpl
import com.retroblade.achievo.utils.*
import dagger.Binds
import dagger.Module
import dagger.Reusable
import javax.inject.Singleton

@Module
interface AppModule {

    @Binds
    @Singleton
    fun bindAuthService(impl: AuthServiceImpl): AuthService

    @Binds
    @Singleton
    fun bindProfileService(impl: ProfileServiceImpl): ProfileService

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    fun bindTokenRepository(impl: TokenRepositoryImpl): TokenRepository

    @Binds
    @Reusable
    fun bindTokenUtils(impl: TokenUtilsImpl): TokenUtils

    @Binds
    @Reusable
    fun bindCryptoHelper(impl: CryptoHelperImpl): CryptoHelper
}