package com.indaco.bart.core.hilt

import com.indaco.bart.data.network.api.BartApi
import com.indaco.bart.data.network.api.BartService
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