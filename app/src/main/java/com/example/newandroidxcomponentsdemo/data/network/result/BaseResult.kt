package com.example.newandroidxcomponentsdemo.data.network.result

open class BaseResult<T>(val result: T? = null, val error: String? = null, val success: Boolean) {

    fun <R>  map(func: (t: T) -> R): BaseResult<R> {
        return if (success) {
            success(func.invoke(result!!))
        } else
            error(error!!)
    }

    companion object {
        fun <T> success(result: T): BaseResult<T> = BaseResult(result, null, true)
        fun <T> error(error: String): BaseResult<T> = BaseResult(null, error, false)
    }
}