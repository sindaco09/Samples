package com.example.samples.data.repository

import com.example.samples.data.models.mockhue.Light
import com.example.samples.data.storage.hue.HueCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HueRepository @Inject constructor(private val hueCache: HueCache) {

    fun addLight(light: Light) {
        hueCache.addLight(light)
    }

    fun getLights(): Flow<List<Light>?> {
        return flowOf(hueCache.getLights())
    }

    fun getLight(uuid: String): Flow<Light?> {
        return flowOf(hueCache.getLight(uuid))
    }

    fun updateLight(light: Light) {
        hueCache.updateLight(light)
    }
}