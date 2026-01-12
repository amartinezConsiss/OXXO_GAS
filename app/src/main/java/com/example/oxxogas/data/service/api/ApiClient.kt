package com.example.oxxogas.data.service.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiClient {

    private val timeOut = 60L

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val okHttpClient = okHttpClient()
        return Retrofit.Builder()
            .baseUrl("https://1947a27799e5.ngrok-free.app/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}