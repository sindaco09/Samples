package com.indaco.samples.data.network.request.bart

import com.indaco.samples.data.network.request.BaseRequest


abstract class BartRequest(
    val cmd: String = "cmd",
    val json: String = "y",
    val key: String = PRIVATE_KEY
): BaseRequest() {

    companion object {
        private const val PRIVATE_KEY = "QE2P-PBTS-9GPT-DWEI"
    }
}