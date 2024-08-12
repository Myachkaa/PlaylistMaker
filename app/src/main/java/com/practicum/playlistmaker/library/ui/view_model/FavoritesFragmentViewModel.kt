package com.practicum.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.db.FavoritesInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(interactor: FavoritesInteractor) : ViewModel() {

    private val stateLiveData: LiveData<FavoritesState> =
        interactor.getFavoriteTracks()
            .map { tracks ->
                if (tracks.isEmpty()) {
                    FavoritesState.Empty
                } else {
                    FavoritesState.Content(tracks)
                }
            }
            .asLiveData()


    private var isClickAllowed = true
    fun observeState(): LiveData<FavoritesState> = stateLiveData
    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L

    }
}
