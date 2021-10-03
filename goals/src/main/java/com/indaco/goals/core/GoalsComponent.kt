package com.indaco.goals.core

import android.content.Context
import com.indaco.goals.ui.screens.GoalsFragment
import com.indaco.goals.ui.screens.children.GoalListFragment
import com.indaco.goals.ui.screens.newgoal.AddGoalDialog
import com.indaco.samples.core.hilt.dependencies.GoalsDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [GoalsDependencies::class],
    modules = [GoalsViewModelModule::class]
)
interface GoalsComponent {

    fun inject(fragment: GoalsFragment)
    fun inject(fragment: GoalListFragment)
    fun inject(fragment: AddGoalDialog)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(dependencies: GoalsDependencies): Builder
        fun build(): GoalsComponent
    }
}