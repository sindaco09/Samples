package com.indaco.samples.data.network.api.news

import androidx.annotation.WorkerThread
import com.indaco.samples.data.models.news.News
import com.indaco.samples.data.network.result.BaseResult
import com.indaco.samples.data.network.result.news.NewsResult
import com.indaco.samples.util.news.MockNewsData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsApi @Inject constructor() {

    @WorkerThread
    fun getBreakingNews(count: Int = 1): BaseResult<NewsResult<News.BreakingNews>> {
        return BaseResult.success(NewsResult(MockNewsData.BreakingNews.getBreakingNewsReelList(count)))
    }

    @WorkerThread
    fun getAlertNews(count: Int = 1): BaseResult<NewsResult<News.AlertNews>> {
        return BaseResult.success(NewsResult(MockNewsData.AlertNews.getAlertNewsReelList(count)))
    }

    @WorkerThread
    fun getLocalNews(count: Int = 1): BaseResult<NewsResult<News.LocalNews>> {
        return BaseResult.success(NewsResult(MockNewsData.LocalNews.getLocalNewsReelList(count)))
    }
}