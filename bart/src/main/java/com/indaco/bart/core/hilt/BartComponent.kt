package com.indaco.bart.core.hilt

import android.content.Context
import com.indaco.bart.ui.screens.BartFragment
import com.indaco.bart.ui.screens.details.BartItemListDialogFragment
import com.indaco.bart.ui.screens.details.BartMapsFragment
import com.indaco.bart.ui.screens.station.BartStationsFragment
import com.indaco.bart.ui.screens.trip.BartTripsFragment
import com.indaco.samples.core.hilt.dependencies.BartDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [BartDependencies::class],
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
        fun appDependencies(dependencies: BartDependencies): Builder
        fun build(): BartComponent
    }
}