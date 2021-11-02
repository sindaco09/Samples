package com.indaco.news.core

import android.content.Context
import com.indaco.samples.core.hilt.dependencies.DatastorePreferencesDependency
import dagger.hilt.android.EntryPointAccessors

object Injector {
    fun from(context: Context): NewsComponent {
        return DaggerNewsComponent
            .builder()
            .context(context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    DatastorePreferencesDependency::class.java
                )
            )
            .build()
    }
}