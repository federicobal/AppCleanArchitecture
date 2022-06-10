package com.project.appcleanarchitecture.util

/**
 * Created by fbal on 29/4/2022.
 */
sealed class Resource<out T> {
    object Loading: Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure(val exception: Exception) : Resource<Nothing>()
}