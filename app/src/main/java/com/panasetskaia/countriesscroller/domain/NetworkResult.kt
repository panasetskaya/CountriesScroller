package com.panasetskaia.countriesscroller.domain

data class NetworkResult<out T>(val status: Status, val data: T?) {

    companion object {
        fun <T> success(data: T): NetworkResult<T> {
            return NetworkResult(Status.SUCCESS, data)
        }
        fun <T> error(data: T): NetworkResult<T> {
            return NetworkResult(Status.ERROR, data)
        }
        fun <T> loading(): NetworkResult<T> {
            return NetworkResult(Status.LOADING, null)
        }
    }
}
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}