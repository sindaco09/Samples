package com.indaco.hue.core.hilt

import android.content.Context
import com.indaco.hue.ui.screens.HueParentFragment
import com.indaco.hue.ui.screens.children.HueConnectionFragment
import com.indaco.hue.ui.screens.children.HueLightsFragment
import com.indaco.samples.core.hilt.dependencies.HueDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [HueDependencies::class],
    modules = [HueViewModelModule::class]
)
interface HueComponent {

    fun inject(fragment: HueParentFragment)
    fun inject(fragment: HueLightsFragment)
    fun inject(fragment: HueConnectionFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(dependencies: HueDependencies): Builder
        fun build(): HueComponent
    }
}