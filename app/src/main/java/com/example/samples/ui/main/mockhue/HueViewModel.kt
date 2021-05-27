package com.example.samples.ui.main.mockhue

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samples.data.models.mockhue.Light
import com.example.samples.data.repository.HueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HueViewModel @Inject constructor(private val hueRepository: HueRepository): ViewModel() {

    private val defaultDispatcher = Dispatchers.Default
    enum class State { NOT_CONNECTED, CONNECTED }

    private var _state = MutableStateFlow(State.NOT_CONNECTED)
    val state: StateFlow<State> get() = _state

    fun discoverBridge() {
        _state.tryEmit(State.CONNECTED)
    }

    fun addLight() {
        val light = Light(name = "my light")
        viewModelScope.launch(defaultDispatcher) {
            hueRepository.addLight(light)
        }
    }
}