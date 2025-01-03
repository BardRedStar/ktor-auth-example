package com.retroblade.achievo.di

import com.retroblade.achievo.data.repository.AuthRepositoryImpl
import com.retroblade.achievo.data.repository.ProfileRepositoryImpl
import com.retroblade.achievo.data.service.TokenService
import com.retroblade.achievo.data.service.TokenServiceImpl
import com.retroblade.achievo.domain.repositories.AuthRepository
import com.retroblade.achievo.domain.repositories.ProfileRepository
import com.retroblade.achievo.domain.usecases.auth.*
import com.retroblade.achievo.domain.usecases.profile.GetProfileUseCase
import com.retroblade.achievo.domain.usecases.profile.GetProfileUseCaseImpl
import com.retroblade.achievo.utils.*
import dagger.Binds
import dagger.Module
import dagger.Reusable
import javax.inject.Singleton

@Module
interface AppModule {

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Reusable
    fun bindGetUserByCredentialsUseCase(impl: GetUserByCredentialsUseCaseImpl): GetUserByCredentialsUseCase

    @Binds
    @Reusable
    fun bindGetUserByEmailUseCase(impl: GetUserByEmailUseCaseImpl): GetUserByEmailUseCase

    @Binds
    @Reusable
    fun bindRegisterUserUseCase(impl: RegisterUserUseCaseImpl): RegisterUserUseCase

    @Binds
    @Reusable
    fun bindCreateJWTTokenUseCase(impl: CreateTokenPairUseCaseImpl): CreateTokenPairUseCase

    @Binds
    @Reusable
    fun bindGetProfileUseCase(impl: GetProfileUseCaseImpl): GetProfileUseCase

    @Binds
    @Reusable
    fun bindValidateRefreshTokenUseCase(impl: ValidateRefreshTokenUseCaseImpl): ValidateRefreshTokenUseCase

    @Binds
    @Reusable
    fun bindValidateAccessTokenUseCase(impl: VerifyAccessTokenUseCaseImpl): VerifyAccessTokenUseCase

    @Binds
    @Reusable
    fun bindJWTHelper(impl: TokenServiceImpl): TokenService

    @Binds
    @Reusable
    fun bindCryptoHelper(impl: CryptoHelperImpl): CryptoHelper
}