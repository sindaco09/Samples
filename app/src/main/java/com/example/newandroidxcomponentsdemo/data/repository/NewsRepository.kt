package com.example.newandroidxcomponentsdemo.data.repository

import com.example.newandroidxcomponentsdemo.data.network.result.BaseResult
import com.example.newandroidxcomponentsdemo.data.models.news.News
import com.example.newandroidxcomponentsdemo.data.storage.news.NewsCache
import com.example.newandroidxcomponentsdemo.data.network.api.news.NewsApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsCache: NewsCache,
    private val newsApi: NewsApi
    ) {

    var breakingNewsCooldown = 5_000L
    var localNewsCooldown = 5_000L

    private var allowBreakingNewsFlow = AtomicBoolean(false)
    private var allowLocalNewsFlow = AtomicBoolean(false)

    fun getNewsFlow(): Flow<BaseResult<List<News>>> {
        return flowOf(newsApi.getBreakingNews())
            .map { it.map { data -> data.news } }
    }

    private fun getBreakingNews(count: Int = 1): BaseResult<List<News.BreakingNews>> {
        return newsApi.getBreakingNews(count).map { data -> data.news }
    }

    suspend fun getBreakingNewsFlow(count: Int = 1) =
        flow {
            allowBreakingNewsFlow.set(true)
            while (allowBreakingNewsFlow.get()) {
                emit(getBreakingNews(count))
                delay(breakingNewsCooldown)
            }
        }

    private fun getLocalNews(): BaseResult<List<News.LocalNews>> {
        return newsApi.getLocalNews().map { data -> data.news}
    }

    suspend fun getLocalNewsFlow() =
        flow {
            allowLocalNewsFlow.set(true)
            while(allowLocalNewsFlow.get()) {
                emit(getLocalNews())
                delay(localNewsCooldown)
            }
        }

    fun interruptBreakingNews() = allowBreakingNewsFlow.set(false)

    fun getInAppNotificationsEnabledMap() = newsCache.getInAppNotificationsEnabledMap()

    fun getPushNotificationsEnabledMap() = newsCache.getPushNotificationsEnabledMap()
}