package com.example.samples.ui.main.mockhue.children

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samples.data.models.mockhue.Light
import com.example.samples.data.repository.HueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HueLightViewModel @Inject constructor(val repository: HueRepository): ViewModel() {

    private val defaultDispatcher = Dispatchers.Default

    fun getLight(uuid: String): Flow<Light?> = repository.getLight(uuid)

    fun getLights(): Flow<List<Light>?> {
        return repository.getLights()
    }

    fun saveLight(light: Light) {
        viewModelScope.launch(defaultDispatcher) {
            repository.updateLight(light)
        }
    }
}