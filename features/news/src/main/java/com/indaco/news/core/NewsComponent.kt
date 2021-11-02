package com.indaco.news.core

import android.content.Context
import com.indaco.news.ui.screens.NewsFragment
import com.indaco.samples.core.hilt.dependencies.DatastorePreferencesDependency
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [DatastorePreferencesDependency::class],
    modules = [NewsViewModelModule::class]
)
interface NewsComponent {

    fun inject(fragment: NewsFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(dependency: DatastorePreferencesDependency): Builder
        fun build(): NewsComponent
    }
}