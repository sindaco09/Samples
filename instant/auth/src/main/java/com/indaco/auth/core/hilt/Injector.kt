package com.indaco.auth.core.hilt

import android.content.Context
import com.indaco.samples.core.hilt.dependencies.AuthDependencies
import dagger.hilt.android.EntryPointAccessors

object Injector {
    fun from(context: Context): AuthComponent {
        return DaggerAuthComponent
            .builder()
            .context(context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    AuthDependencies::class.java
                )
            )
            .build()
    }
}