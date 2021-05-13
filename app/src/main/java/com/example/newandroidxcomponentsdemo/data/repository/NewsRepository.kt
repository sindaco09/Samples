package com.example.newandroidxcomponentsdemo.data.repository

import android.util.Log
import com.example.newandroidxcomponentsdemo.data.network.result.BaseResult
import com.example.newandroidxcomponentsdemo.data.models.news.News
import com.indaco.myhomeapp.data.network.api.news.NewsApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(private val newsApi: NewsApi) {

    var breakingNewsCooldown = 5_000L

    private var allowNewsFlow = AtomicBoolean(false)

    fun getNewsFlow(): Flow<BaseResult<List<News>>> {
        return flowOf(newsApi.getBreakingNews())
            .map { it.map { data -> data.news } }
    }

    fun getBreakingNews(count: Int = 2): BaseResult<List<News>> {
        return newsApi.getBreakingNews(count).map { data -> data.news }
    }

    suspend fun getBreakingNewsFlow(count: Int = 2) =
        flow {
            allowNewsFlow.set(true)
            while (allowNewsFlow.get()) {
                emit(getBreakingNews(count))
                delay(breakingNewsCooldown)
                Log.d("TAG","breakingNewsFlow")
            }
        }

    fun interruptBreakingNews() = allowNewsFlow.set(false)


}