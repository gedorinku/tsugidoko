package io.github.hunachi.tsugidoko.util

sealed class NetworkState<T> {

    class Success<T>(val result: T) : NetworkState<T>()

    class Error<T>(val e: Exception) : NetworkState<T>()
}