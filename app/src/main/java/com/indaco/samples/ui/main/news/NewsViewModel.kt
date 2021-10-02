package com.indaco.samples.ui.main.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indaco.samples.data.models.news.News
import com.indaco.samples.data.network.result.BaseResult
import com.indaco.samples.data.repository.NewsRepository
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

    private var breakingNewsJob: Job? = null

    private val _breakingNewsLiveDataStream = MutableLiveData<BaseResult<List<News.BreakingNews>>>()
    val breakingNewsLiveStream: LiveData<BaseResult<List<News.BreakingNews>>> get() = _breakingNewsLiveDataStream

    private val _localNewsLiveDataStream = MutableLiveData<BaseResult<List<News.LocalNews>>>()
    val localNewsLiveStream: LiveData<BaseResult<List<News.LocalNews>>> get() = _localNewsLiveDataStream

    fun getBreakingNews() {
        if (breakingNewsJob == null || breakingNewsJob!!.isCancelled)
            breakingNewsJob = viewModelScope.launch(ioDispatcher) {
                newsRepository.getBreakingNewsFlow(1).collect {
                    _breakingNewsLiveDataStream.postValue(it)
                }
            }
    }

    fun stopBreakingNews() {
        newsRepository.interruptBreakingNews()
        breakingNewsJob?.cancel()
    }

    fun getLocalNews() {
        viewModelScope.launch(ioDispatcher) {
            newsRepository.getLocalNewsFlow().collect {
                _localNewsLiveDataStream.postValue(it)
            }
        }
    }

    fun getInAppNotificationsEnabledMap() {
        viewModelScope.launch(ioDispatcher) {
            val flow = newsRepository.getInAppNotificationsEnabledMap()
            flow.collect { map ->
                map.forEach { (key, value) ->
                    Log.d("TAG","inApp notifications enabled: $key, $value")
                }
            }
        }

    }
}