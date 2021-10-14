package com.indaco.auth.core.hilt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.indaco.auth.ui.screens.login.LoginViewModel
import com.indaco.auth.ui.screens.signup.SignUpViewModel
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
    @ViewModelKey(LoginViewModel::class)
    abstract fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun provideSignUpViewModel(viewModel: SignUpViewModel): ViewModel
}