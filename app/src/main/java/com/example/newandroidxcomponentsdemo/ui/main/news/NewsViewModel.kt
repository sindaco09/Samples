package com.example.newandroidxcomponentsdemo.ui.main.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newandroidxcomponentsdemo.data.models.news.News
import com.example.newandroidxcomponentsdemo.data.network.result.BaseResult
import com.example.newandroidxcomponentsdemo.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private var newsJob: Job? = null

    var newsCount = MutableLiveData("0")

    private val _newsLiveDataStream = MutableLiveData<BaseResult<List<News>>>()
    val newsLiveStream: LiveData<BaseResult<List<News>>> get() = _newsLiveDataStream

    fun getBreakingNews() {
        if (newsJob == null || newsJob!!.isCancelled)
            newsJob = viewModelScope.launch(ioDispatcher) {
                newsRepository.getBreakingNewsFlow().collect {
                    _newsLiveDataStream.postValue(it)
                }
            }
    }

    fun stopBreakingNews() {
        newsRepository.interruptBreakingNews()
        newsJob?.cancel()
    }

    fun getInAppNotificationsEnabledSet() {
        val set = newsRepository.getInAppNotificationsEnabledSet()
        set.forEach { key, value ->
            Log.d("TAG","inApp notifications enabled: $key, $value")
        }
    }

    fun increaseNewsCount() {
//        val value = newsCount.value?:0 + 1
//        newsCount.postValue(value.toString())
    }

    fun decreaseNewsCount() {
//        var value: Int = newsCount.value?:0 - 1
//        if (value < 0)
//            value = 0
//        newsCount.postValue(value.toString())
    }

}