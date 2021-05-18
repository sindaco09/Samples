package com.example.samples.data.network.result.news

import com.example.samples.data.models.news.News

class NewsResult<T: News>(val news: List<T>)