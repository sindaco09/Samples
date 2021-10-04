package com.indaco.news.data.network.api

import android.content.Context
import androidx.annotation.WorkerThread
import com.indaco.news.data.models.News
import com.indaco.news.data.network.result.NewsResult
import com.indaco.news.util.MockNewsData
import com.indaco.samples.data.network.result.BaseResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsApi @Inject constructor(@ApplicationContext val app: Context) {

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
        return BaseResult.success(NewsResult(MockNewsData.LocalNews.getLocalNewsReelList(count, app)))
    }
}