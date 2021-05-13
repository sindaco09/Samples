package com.indaco.myhomeapp.data.network.api.news

import androidx.annotation.WorkerThread
import com.example.newandroidxcomponentsdemo.data.network.result.BaseResult
import com.example.newandroidxcomponentsdemo.data.network.result.news.NewsResult
import com.indaco.myhomeapp.util.mock.news.MockNewsData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsApi @Inject constructor() {

    @WorkerThread
    fun getBreakingNews(count: Int = 2): BaseResult<NewsResult> {
        return BaseResult.success(NewsResult(MockNewsData.getBreakingNewsReelList(count)))
    }

}