package com.indaco.samples.core.hilt.dependencies

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@EntryPoint
@InstallIn(SingletonComponent::class)
interface RetrofitDependency {
    fun retrofit(): Retrofit
}