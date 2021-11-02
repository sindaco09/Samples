package com.indaco.corebart.core.hilt.dependencies

import com.indaco.corebart.dao.BartDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BartDaoDependency {
    fun bartDao(): BartDao
}