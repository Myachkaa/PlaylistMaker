package com.practicum.playlistmaker.search.data.impl

import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.network.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksResponse
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.util.SearchResult
import com.practicum.playlistmaker.search.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient, private val gson: Gson) :
    TrackRepository {

    override fun searchTrack(expression: String): SearchResult<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            200 -> {
                SearchResult.Success((response as TracksResponse).tracks.map {
                    Track(
                        it.trackName ?: "",
                        it.artistName ?: "",
                        it.trackTime ?: 0L,
                        it.artworkUrl100 ?: "",
                        it.collectionName ?: "",
                        it.releaseDate ?: "",
                        it.primaryGenreName ?: "",
                        it.country ?: "",
                        it.previewUrl ?: ""
                    )
                })
            }

            -1 -> {
                SearchResult.Error(-1)
            }

            else -> {
                SearchResult.Error(0)
            }
        }
    }

    override fun toJson(track: Track): String {
        return gson.toJson(track)
    }
}