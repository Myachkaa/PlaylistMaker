package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.SearchResult
import kotlinx.coroutines.flow.Flow


interface TrackRepository {
    fun searchTrack(expression: String): Flow<SearchResult<List<Track>>>
}
