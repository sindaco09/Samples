package com.indaco.camera.core.hilt

import android.content.Context
import com.indaco.camera.ui.screens.types.ImageToTextCameraXFragment
import com.indaco.camera.ui.screens.types.QRCamera2Fragment
import com.indaco.camera.ui.screens.types.QRCameraXFragment
import com.indaco.samples.core.hilt.dependencies.DispatcherDependency
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [DispatcherDependency::class],
    modules = [CameraViewModelModule::class]
)
interface CameraComponent {

    fun inject(fragment: QRCamera2Fragment)
    fun inject(fragment: QRCameraXFragment)
    fun inject(fragment: ImageToTextCameraXFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun dispatcherDependency(dependency: DispatcherDependency): Builder
        fun build(): CameraComponent
    }
}