package com.indaco.camera.core.hilt

import android.content.Context
import com.indaco.samples.core.hilt.dependencies.CameraDependencies
import dagger.hilt.android.EntryPointAccessors

object Injector {
    fun from(context: Context): CameraComponent {
        return DaggerCameraComponent
            .builder()
            .context(context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    CameraDependencies::class.java
                )
            )
            .build()
    }
}