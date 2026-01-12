package com.example.oxxogas.data.service.module

import com.example.oxxogas.data.service.api.services.OxxoGasApi
import com.example.oxxogas.data.service.repositoryimpl.SendEmailRepositoryImpl
import com.example.oxxogas.domain.repository.SendEmailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SendEmailModule {

    @Singleton
    @Provides
    fun providesService(retrofit: Retrofit): OxxoGasApi =
        retrofit.create(OxxoGasApi::class.java)

    @Singleton
    @Provides
    fun providesSendEmailRepository(sendEmailRepositoryImpl: SendEmailRepositoryImpl): SendEmailRepository =
        sendEmailRepositoryImpl
}