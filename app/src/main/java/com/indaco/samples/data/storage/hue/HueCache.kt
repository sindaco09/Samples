package com.indaco.samples.data.storage.hue

import com.indaco.samples.data.models.mockhue.Light
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HueCache @Inject constructor (private val hueDao: HueDao) {

    fun addLight(light: Light) = hueDao.addLight(light)

    fun getLights(): List<Light>? = hueDao.getLights()

    fun getLight(uuid: String): Light? = hueDao.getLight(uuid)

    fun updateLight(light: Light) {
        hueDao.updateLight(light)
    }
}