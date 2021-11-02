package com.indaco.goals.core

import android.content.Context
import com.indaco.core.core.hilt.dependencies.GoalsDaoDependency
import dagger.hilt.android.EntryPointAccessors

object Injector {
    fun from(context: Context): GoalsComponent {
        return DaggerGoalsComponent
            .builder()
            .context(context)
            .goalsDaoDependency(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    GoalsDaoDependency::class.java
                )
            )
            .build()
    }
}