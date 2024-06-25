package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.SearchResult

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: SearchResult<List<Track>>)
    }

    fun saveTrack(track: Track)
    fun getSearchHistory(): List<Track>
    fun clearSearchHistory()
}