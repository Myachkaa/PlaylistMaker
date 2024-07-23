package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(
    private val trackRepository: TrackRepository,
    private val historyRepository: SearchHistoryRepository
) : TrackInteractor {
    override fun searchTrack(expression: String): Flow<Pair<List<Track>?, Int?>> {
        return trackRepository.searchTrack(expression).map { result ->
            when (result) {
                is SearchResult.Success -> {
                    Pair(result.data, null)
                }

                is SearchResult.Error -> {
                    Pair(null, result.code)
                }
            }
        }

    }

    override fun saveTrack(track: Track) {
        historyRepository.saveTrack(track)
    }

    override fun getSearchHistory(): List<Track> {
        return historyRepository.getSearchHistory()
    }

    override fun clearSearchHistory() {
        historyRepository.clearSearchHistory()
    }
}