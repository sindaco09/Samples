package com.indaco.hue.ui.screens.children

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indaco.samples.data.models.mockhue.Light
import com.indaco.hue.data.repository.HueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

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