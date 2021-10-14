package com.indaco.auth.core.hilt

import android.content.Context
import com.indaco.auth.ui.screens.login.LoginFragment
import com.indaco.auth.ui.screens.signup.AccountDetailsFragment
import com.indaco.auth.ui.screens.signup.EmailFragment
import com.indaco.auth.ui.screens.signup.PasswordFragment
import com.indaco.auth.ui.screens.signup.SignUpFragment
import com.indaco.samples.core.hilt.dependencies.AuthDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [AuthDependencies::class],
    modules = [AuthViewModelModule::class]
)
interface AuthComponent {

    fun inject(fragment: EmailFragment)
    fun inject(fragment: AccountDetailsFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: PasswordFragment)
    fun inject(fragment: SignUpFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(dependencies: AuthDependencies): Builder
        fun build(): AuthComponent
    }
}