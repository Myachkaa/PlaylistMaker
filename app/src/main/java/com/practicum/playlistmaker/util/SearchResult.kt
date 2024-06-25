package com.practicum.playlistmaker.util

sealed class SearchResult<T>(val data: T? = null, val code: Int? = null) {
    class Success<T>(data: T) : SearchResult<T>(data)
    class Error<T>(code: Int?, data: T? = null) : SearchResult<T>(data, code)
}