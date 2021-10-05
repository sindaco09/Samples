package com.indaco.auth.core.hilt

import android.content.Context
import com.indaco.auth.ui.screens.AboutMeFragment
import com.indaco.auth.ui.screens.LoginFragment
import com.indaco.auth.ui.screens.PasswordFragment
import com.indaco.auth.ui.screens.UsernameFragment
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

    fun inject(fragment: UsernameFragment)
    fun inject(fragment: PasswordFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: AboutMeFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(dependencies: AuthDependencies): Builder
        fun build(): AuthComponent
    }
}