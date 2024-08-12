package com.practicum.playlistmaker.library.ui.view_model

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavoritesState {
    data class Content(
        val tracks: List<Track>
    ) : FavoritesState

    data object Empty : FavoritesState
}