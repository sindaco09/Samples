package com.indaco.camera.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraRepository @Inject constructor() {

    suspend fun processQRCode(code: String?): Flow<String?> {
        return flow<String?> {
            delay(4_000)

            emit(code)
        }
    }

}