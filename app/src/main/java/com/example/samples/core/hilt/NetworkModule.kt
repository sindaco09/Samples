package com.example.samples.core.hilt

import com.example.samples.BuildConfig
import com.example.samples.data.network.api.bart.BartService
import com.example.samples.data.network.api.user.UserService
import com.example.samples.util.mock.MockInterceptor
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
    @Singleton
    fun provideMockInterceptor(): MockInterceptor = MockInterceptor()

    @Provides
    fun provideOkHttpClient(mockInterceptor: MockInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(mockInterceptor)
        }
        return builder.build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    // Only need to provide for interfaces
    @Provides
    fun provideBartService(retrofit: Retrofit): BartService =
        retrofit.create(BartService::class.java)

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)
}