package com.indaco.camera.core.hilt

import android.content.Context
import com.indaco.samples.core.hilt.dependencies.DispatcherDependency
import dagger.hilt.android.EntryPointAccessors

object Injector {
    fun from(context: Context): CameraComponent {
        return DaggerCameraComponent
            .builder()
            .context(context)
            .dispatcherDependency(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    DispatcherDependency::class.java
                )
            )
            .build()
    }
}