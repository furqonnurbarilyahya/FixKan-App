package com.practice.fixkan.data

sealed class Result<out R> private constructor(){
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val error: String): Result<Nothing>()
    data class Loading<T>(val isLoading: Boolean): Result<T>()
}