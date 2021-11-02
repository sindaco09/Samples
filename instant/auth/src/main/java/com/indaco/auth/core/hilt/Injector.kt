package com.indaco.auth.core.hilt

import android.content.Context
import com.indaco.core.core.hilt.dependencies.UserDaoDependency
import com.indaco.samples.core.hilt.dependencies.CurrentUserDependency
import com.indaco.samples.core.hilt.dependencies.DispatcherDependency
import dagger.hilt.android.EntryPointAccessors

object Injector {
    fun from(context: Context): AuthComponent {
        return DaggerAuthComponent
            .builder()
            .context(context)
            .dispatcherDependency(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    DispatcherDependency::class.java
                )
            )
            .currentUserDependency(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    CurrentUserDependency::class.java
                )
            )
            .userDaoDependency(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    UserDaoDependency::class.java
                )
            )
            .build()
    }
}