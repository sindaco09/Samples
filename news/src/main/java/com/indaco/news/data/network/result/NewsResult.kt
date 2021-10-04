package com.indaco.news.data.network.result

import com.indaco.news.data.models.News

class NewsResult<T: News>(val news: List<T>)