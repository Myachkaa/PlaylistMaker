package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.SearchResult


interface TrackRepository {
    fun searchTrack(expression: String): SearchResult<List<Track>>
}
