package com.example.samples.data.network.request

abstract class BaseRequest {

    open fun toMap(vararg values: Pair<String, String?>): Map<String, String?> {
        return mapOf(*values)
    }
}