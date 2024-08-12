package com.practicum.playlistmaker.search.ui.view_model

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface SearchState {

    object Default : SearchState

    object Loading : SearchState

    data class TrackList(val tracks: List<Track>) : SearchState

    data class HistoryTrackList(val tracks: List<Track>) : SearchState

    object ServerError : SearchState

    object NotFound : SearchState
}