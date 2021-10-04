package com.indaco.bart.core.hilt

import android.content.Context
import com.indaco.samples.core.hilt.dependencies.BartDependencies
import dagger.hilt.android.EntryPointAccessors

object Injector {
    fun from(context: Context): BartComponent {
        return DaggerBartComponent
            .builder()
            .context(context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    BartDependencies::class.java
                )
            )
            .build()
    }
}