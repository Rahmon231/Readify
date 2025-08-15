package com.dev.readify.data

sealed class BookState<out T> {
    data object Loading : BookState<Nothing>()
    data class Success<T>(val data: T) : BookState<T>()
    data class Failure(val throwable: Throwable) : BookState<Nothing>()
}