package com.indaco.samples.data.repository

import com.indaco.samples.data.network.result.BaseResult
import com.indaco.samples.data.models.news.News
import com.indaco.samples.data.storage.news.NewsCache
import com.indaco.samples.data.network.api.news.NewsApi
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

    private fun getBreakingNews(count: Int = 1): BaseResult<List<News.BreakingNews>> {
        return newsApi.getBreakingNews(count).map { data -> data.news }
    }

    /*
     * Simulate listening for "Breaking news" from an endpoint every cooldown period
     * allowBreakingNewsFlow is a controllable switch. Eventually meant to test
     */
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