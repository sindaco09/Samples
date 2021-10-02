package com.indaco.samples.data.network.result.news

import com.indaco.samples.data.models.news.News

class NewsResult<T: News>(val news: List<T>)