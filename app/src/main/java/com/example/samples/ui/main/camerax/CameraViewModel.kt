package com.example.samples.ui.main.camerax

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samples.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val userRepository: UserRepository
    ): ViewModel() {

    fun processQRCode(code: String?): LiveData<String?> {
        return MutableLiveData<String?>().also {
            viewModelScope.launch(dispatcher) {
                userRepository.processQRCode(code).collect { result: String? ->
                    Log.d("TAG","processQRCode result: ${result}")
                    it.postValue(result)
                }
            }
        }
    }
}