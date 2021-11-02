package com.indaco.hue.core.hilt

import android.content.Context
import com.indaco.core.core.hilt.dependencies.HueDaoDependency
import dagger.hilt.android.EntryPointAccessors

object Injector {
    fun from(context: Context): HueComponent {
        return DaggerHueComponent
            .builder()
            .context(context)
            .hueDaoDependency(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,HueDaoDependency::class.java)
            )
            .build()
    }
}