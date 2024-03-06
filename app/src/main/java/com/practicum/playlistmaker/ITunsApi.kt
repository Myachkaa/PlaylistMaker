package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunsApi {
    @GET("/search?entity=song")
    fun getTracks(@Query("term") term: String) : Call<TracksResponse>
}