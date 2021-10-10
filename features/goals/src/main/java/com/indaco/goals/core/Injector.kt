package com.indaco.goals.core

import android.content.Context
import com.indaco.samples.core.hilt.dependencies.GoalsDependencies
import dagger.hilt.android.EntryPointAccessors

object Injector {
    fun from(context: Context): GoalsComponent {
        return DaggerGoalsComponent
            .builder()
            .context(context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    GoalsDependencies::class.java
                )
            )
            .build()
    }
}