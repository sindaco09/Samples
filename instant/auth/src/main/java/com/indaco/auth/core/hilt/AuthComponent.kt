package com.indaco.auth.core.hilt

import android.content.Context
import com.indaco.auth.ui.screens.login.LoginFragment
import com.indaco.auth.ui.screens.signup.AccountDetailsFragment
import com.indaco.auth.ui.screens.signup.EmailFragment
import com.indaco.auth.ui.screens.signup.PasswordFragment
import com.indaco.auth.ui.screens.signup.SignUpFragment
import com.indaco.core.core.hilt.dependencies.UserDaoDependency
import com.indaco.samples.core.hilt.dependencies.CurrentUserDependency
import com.indaco.samples.core.hilt.dependencies.DispatcherDependency
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [DispatcherDependency::class, CurrentUserDependency::class, UserDaoDependency::class],
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
        fun dispatcherDependency(d0: DispatcherDependency): Builder
        fun currentUserDependency(d1: CurrentUserDependency): Builder
        fun userDaoDependency(d2: UserDaoDependency): Builder
        fun build(): AuthComponent
    }
}