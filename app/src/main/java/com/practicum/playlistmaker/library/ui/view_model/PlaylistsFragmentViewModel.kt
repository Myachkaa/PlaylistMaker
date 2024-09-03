package com.practicum.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    init {
        fetchPlaylists()
    }

    private fun fetchPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { playlistList ->
                _playlists.postValue(playlistList)
            }
        }
    }
}