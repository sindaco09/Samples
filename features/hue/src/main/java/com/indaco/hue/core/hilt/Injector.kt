package com.indaco.hue.core.hilt

import android.content.Context
import com.indaco.samples.core.hilt.dependencies.HueDependencies
import dagger.hilt.android.EntryPointAccessors

object Injector {
    fun from(context: Context): HueComponent {
        return DaggerHueComponent
            .builder()
            .context(context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    HueDependencies::class.java
                )
            )
            .build()
    }
}