package com.example.samples.core.hilt

import com.example.samples.data.network.api.bart.BartApi
import com.example.samples.data.network.api.bart.BartService
import com.example.samples.data.repository.BartRepository
import com.example.samples.data.storage.bart.BartCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        // This should never be baseUrl because the baseUrl changes depending on api
        // being used
        private const val baseUrl = "https://www.google.com"
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideBartApi(retrofit: Retrofit) =
        BartApi(retrofit.create(BartService::class.java))

    @Provides
    @Singleton
    fun provideBartRepository(bartApi: BartApi, bartCache: BartCache) =
        BartRepository(bartApi, bartCache)

}