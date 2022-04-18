package net.flow.jetpackmvvm.ext.util

import kotlinx.coroutines.flow.flow

fun <T> getFlow(block: suspend () -> T): kotlinx.coroutines.flow.Flow<T> {
    return flow {
        emit(block())
    }
}