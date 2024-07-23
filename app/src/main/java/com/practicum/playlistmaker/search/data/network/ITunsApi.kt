package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunsApi {
    @GET("/search?entity=song")
    suspend fun getTracks(@Query("term") term: String): TracksResponse
}