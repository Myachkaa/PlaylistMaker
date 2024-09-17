package com.practicum.playlistmaker.library.ui.view_model

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.api.CreatePlaylistInteractor
import com.practicum.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor
) : ViewModel() {

    protected val _playlistCreated = MutableLiveData<Boolean>()
    val playlistCreated: LiveData<Boolean> get() = _playlistCreated

    protected val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    protected val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    protected val _coverImageUri = MutableLiveData<Uri?>()
    val coverImageUri: LiveData<Uri?> get() = _coverImageUri
    fun createPlaylist(name: String, description: String?, coverImagePath: String?) {
        viewModelScope.launch {
            try {
                val playlist = Playlist(
                    id = 0,
                    name = name,
                    description = description,
                    coverImagePath = coverImagePath,
                    trackIds = "",
                    trackCount = 0
                )

                createPlaylistInteractor.addPlaylist(playlist)

                _playlistCreated.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _playlistCreated.postValue(false)
            }
        }
    }

    fun copyImageToPrivateStorage(context: Context, uri: Uri): Uri? {
        return createPlaylistInteractor.copyImageToPrivateStorage(context, uri)
    }
}