package com.panasetskaia.countriesscroller.domain

data class NetworkResult<out T>(val status: Status, val data: T?, val msg: String?) {

    companion object {
        fun <T> success(data: T): NetworkResult<T> {
            return NetworkResult(Status.SUCCESS, data, null)
        }
        fun <T> error(msg: String?): NetworkResult<T> {
            return NetworkResult(Status.ERROR, null, msg)
        }
        fun <T> loading(): NetworkResult<T> {
            return NetworkResult(Status.LOADING, null, null)
        }
    }
}
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}