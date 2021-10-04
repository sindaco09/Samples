package com.indaco.bart.core.hilt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.indaco.bart.ui.screens.BartViewModel
import com.indaco.bart.ui.screens.details.BartDetailsViewModel
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.core.hilt.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@InstallIn(SingletonComponent::class)
@Module
abstract class BartViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BartViewModel::class)
    abstract fun provideBartViewModel(viewModel: BartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BartDetailsViewModel::class)
    abstract fun provideBartDetailsViewModel(viewModel: BartDetailsViewModel): ViewModel
}