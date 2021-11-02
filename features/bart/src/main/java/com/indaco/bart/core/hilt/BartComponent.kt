package com.indaco.bart.core.hilt

import android.content.Context
import com.indaco.bart.ui.screens.BartFragment
import com.indaco.bart.ui.screens.details.BartItemListDialogFragment
import com.indaco.bart.ui.screens.details.BartMapsFragment
import com.indaco.bart.ui.screens.station.BartStationsFragment
import com.indaco.bart.ui.screens.trip.BartTripsFragment
import com.indaco.corebart.core.hilt.dependencies.BartDaoDependency
import com.indaco.samples.core.hilt.dependencies.DispatcherDependency
import com.indaco.samples.core.hilt.dependencies.RetrofitDependency
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [BartDaoDependency::class, RetrofitDependency::class, DispatcherDependency::class],
    modules = [BartViewModelModule::class, BartApiModule::class]
)
interface BartComponent {

    fun inject(fragment: BartFragment)
    fun inject(fragment: BartTripsFragment)
    fun inject(fragment: BartStationsFragment)
    fun inject(fragment: BartItemListDialogFragment)
    fun inject(fragment: BartMapsFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun bartDaoDependency(d: BartDaoDependency): Builder
        fun retrofitDependency(d: RetrofitDependency): Builder
        fun dispatcherDependency(d: DispatcherDependency): Builder
        fun build(): BartComponent
    }
}