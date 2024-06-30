package auth_sample.di

import auth_sample.data.repository.AuthRepositoryImpl
import auth_sample.data.repository.ProfileRepositoryImpl
import auth_sample.domain.repositories.AuthRepository
import auth_sample.domain.repositories.ProfileRepository
import auth_sample.domain.usecases.auth.*
import auth_sample.domain.usecases.profile.GetProfileUseCase
import auth_sample.domain.usecases.profile.GetProfileUseCaseImpl
import auth_sample.utils.CryptoHelper
import auth_sample.utils.CryptoHelperImpl
import auth_sample.utils.JWTHelper
import auth_sample.utils.JWTHelperImpl
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
    fun bindCreateJWTTokenUseCase(impl: CreateJWTTokenUseCaseImpl): CreateJWTTokenUseCase

    @Binds
    @Reusable
    fun bindGetProfileUseCase(impl: GetProfileUseCaseImpl): GetProfileUseCase

    @Binds
    @Reusable
    fun bindJWTHelper(impl: JWTHelperImpl): JWTHelper

    @Binds
    @Reusable
    fun bindCryptoHelper(impl: CryptoHelperImpl): CryptoHelper
}