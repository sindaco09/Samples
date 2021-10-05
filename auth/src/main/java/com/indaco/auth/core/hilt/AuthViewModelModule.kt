package com.indaco.auth.core.hilt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.indaco.auth.ui.screens.AuthViewModel
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.core.hilt.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@InstallIn(SingletonComponent::class)
@Module
abstract class AuthViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun provideAuthViewModel(viewModel: AuthViewModel): ViewModel
}