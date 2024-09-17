package com.practicum.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.api.CreatePlaylistInteractor
import com.practicum.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val interactor: CreatePlaylistInteractor,
) : CreatePlaylistViewModel(interactor) {

    private var playlist: Playlist? = null
    private fun setPlaylistForEditing(playlist: Playlist) {
        this.playlist = playlist
        _name.value = playlist.name
        _description.value = playlist.description ?: ""
        _coverImageUri.value = if (!playlist.coverImagePath.isNullOrEmpty()) {
            Uri.parse(playlist.coverImagePath)
        } else {
            null
        }
    }

    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            val loadedPlaylist = interactor.getPlaylistById(playlistId)
            if (loadedPlaylist != null) {
                setPlaylistForEditing(loadedPlaylist)
            }
        }
    }

    fun savePlaylist(name: String, description: String?, coverImagePath: String?) {
        viewModelScope.launch {
            try {
                val updatedPlaylist = playlist?.copy(
                    name = name,
                    description = description,
                    coverImagePath = coverImagePath ?: playlist?.coverImagePath
                )
                if (updatedPlaylist != null) {
                    interactor.updatePlaylist(updatedPlaylist)
                    _playlistCreated.postValue(true)
                } else {
                    _playlistCreated.postValue(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _playlistCreated.postValue(false)
            }
        }
    }
}