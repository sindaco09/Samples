package com.example.samples.util.mock

import android.util.Log
import com.example.samples.App
import com.example.samples.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import javax.inject.Singleton

/*
 * Interceptor for testing endpoints prior to being merged into develop
 */
@Singleton
class MockInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {

            val endpointToTest = chain.request().url.pathSegments.last()

            val responseToTest = responseToTest(endpointToTest)

            if (!responseToTest.first) {
                if (responseToTest.second != null)
                    Log.e(TAG,"error: ${responseToTest.second}")

                return chain.proceed(chain.request())
            } else {
                val mockResponse = responseToTest.second!!

                return chain.proceed(chain.request())
                    .newBuilder()
                    .code(code)
                    .protocol(Protocol.HTTP_2)
                    .message(mockResponse)
                    .body(mockResponse.toByteArray().toResponseBody(CONTENT_TYPE_JSON.toMediaTypeOrNull()))
                    .addHeader(CONTENT_TYPE, CONTENT_TYPE_JSON)
                    .build()
            }

        } else {
            Log.e(TAG, "MockInterceptor is only meant for Testing Purposes and " +
                    "bound to be used only with DEBUG mode in DEV flavor")
            return chain.proceed(chain.request())
        }
    }

    private fun responseToTest(capturedEndpoint: String?): Pair<Boolean, String?> {
        val appInstance = App.Instance
        return when {
            interceptValuesNotSet() -> Pair(false, "intercept values not set")
            capturedEndpoint.isNullOrEmpty() -> Pair(false, "Null endpoint")
            capturedEndpoint == endpoint -> Pair(true, getJsonDataFromAsset(appInstance, "$NETWORK_RESPONSES_DIR/$jsonFileName"))
            else -> Pair(false, null)
        }
    }

    private fun getJsonDataFromAsset(context: App, fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            Log.e("TAG", "failed to get jsonData from string")
            null
        }
    }

    private fun interceptValuesNotSet() = endpoint.isEmpty() || jsonFileName.isEmpty()

    companion object {
        private const val TAG = "MockInterceptor"
        private const val NETWORK_RESPONSES_DIR = "network_responses"
        private const val CONTENT_TYPE = "content-type"
        private const val CONTENT_TYPE_JSON = "application/json"
        private const val SUCCESS_CODE = 200

        private var endpoint = ""
        private var jsonFileName = ""
        private var code = SUCCESS_CODE

        // Use this function in API Classes prior to "execute" to intercept the endpoint
        fun interceptEndpoint(_endpoint: String, _jsonFileName: String, code: Int = SUCCESS_CODE) {
            val endpoint = _endpoint.split("/").last()
            this.endpoint = endpoint
            this.jsonFileName = if(_jsonFileName.contains(".json")) _jsonFileName else "$_jsonFileName.json"
            this.code = code
        }
    }
}