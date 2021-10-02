package com.indaco.samples.data.network.request

abstract class BaseRequest {

    // for passing a class as a map to pass as retrofit @Body
    open fun toMap(vararg values: Pair<String, String?>): Map<String, String?> {
        return mapOf(*values)
    }
}