package com.indaco.goals.core

import android.content.Context
import com.indaco.core.core.hilt.dependencies.GoalsDaoDependency
import com.indaco.goals.ui.screens.GoalsFragment
import com.indaco.goals.ui.screens.children.GoalListFragment
import com.indaco.goals.ui.screens.newgoal.AddGoalDialog
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [GoalsDaoDependency::class],
    modules = [GoalsViewModelModule::class]
)
interface GoalsComponent {

    fun inject(fragment: GoalsFragment)
    fun inject(fragment: GoalListFragment)
    fun inject(fragment: AddGoalDialog)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun goalsDaoDependency(dependencies: GoalsDaoDependency): Builder
        fun build(): GoalsComponent
    }
}