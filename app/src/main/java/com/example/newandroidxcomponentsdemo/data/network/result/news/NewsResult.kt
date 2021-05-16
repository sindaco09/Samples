package com.example.newandroidxcomponentsdemo.data.network.result.news

import com.example.newandroidxcomponentsdemo.data.models.news.News

class NewsResult<T: News>(val news: List<T>)