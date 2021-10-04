package com.indaco.hue.core.hilt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.indaco.hue.ui.screens.HueViewModel
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.core.hilt.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@InstallIn(SingletonComponent::class)
@Module
abstract class HueViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HueViewModel::class)
    abstract fun provideHueViewModel(viewModel: HueViewModel): ViewModel
}