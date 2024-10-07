package com.example.mvisampleapp.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import com.slack.circuit.retained.rememberRetained

sealed interface Async<out T> {
    object None : Async<Nothing>

    object Loading : Async<Nothing>

    data class Success<T>(
        val data: T
    ) : Async<T>

    data class Fail<T>(
        val throwable: Throwable
    ) : Async<T>
}

@Composable
fun <T> produceAsync(
    async: Async<T>,
    producer: suspend () -> T,
    onSuccess: (T) -> Async<T>,
    onFail: (Throwable) -> Async<T>
): Async<T> {
    if(async is Async.Loading) {
        val result by produceState<T?>(initialValue = null) {
            value = try {
                producer()
            } catch (throwable: Throwable) {
                onFail(throwable)
                null
            }
        }

        val throwable by rememberRetained {
            mutableStateOf<Throwable?>(null)
        }

        result?.let { value ->
            onSuccess(value)
            return Async.Success(
                data = value
            )
        }

        throwable?.let { exception ->
            return Async.Fail(exception)
        }
    }

    return async
}