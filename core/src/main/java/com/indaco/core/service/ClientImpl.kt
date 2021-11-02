package com.indaco.core.service

import okhttp3.OkHttpClient
import javax.inject.Inject

class ClientImpl @Inject constructor(val okClient: OkHttpClient): Client {
    override fun execute(request: BaseRequest){

    }
}