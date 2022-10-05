package com.indaco.samples.core.hilt.network

import com.indaco.samples.data.network.api.bart.BartApi
import com.indaco.samples.data.network.api.bart.BartService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class BartApiModule {

    @Provides
    fun provideBartApi(retrofit: Retrofit) =
        BartApi(retrofit.create(BartService::class.java))
}