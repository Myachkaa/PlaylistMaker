package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import java.util.concurrent.Executors

class TrackInteractorImpl(
    private val trackRepository: TrackRepository,
    private val historyRepository: SearchHistoryRepository
) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(trackRepository.searchTrack(expression))
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