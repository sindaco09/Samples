package com.indaco.bart.core.hilt

import android.content.Context
import com.indaco.corebart.core.hilt.dependencies.BartDaoDependency
import com.indaco.samples.core.hilt.dependencies.DispatcherDependency
import com.indaco.samples.core.hilt.dependencies.RetrofitDependency
import dagger.hilt.android.EntryPointAccessors

object Injector {
    fun from(context: Context): BartComponent {
        return DaggerBartComponent
            .builder()
            .context(context)
            .bartDaoDependency(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    BartDaoDependency::class.java
                )
            )
            .dispatcherDependency(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    DispatcherDependency::class.java
                )
            )
            .retrofitDependency(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    RetrofitDependency::class.java
                )
            )
            .build()
    }
}