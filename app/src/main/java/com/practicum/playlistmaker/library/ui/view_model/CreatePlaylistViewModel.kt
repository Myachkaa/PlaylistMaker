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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CreatePlaylistViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor
) : ViewModel() {

    private val _playlistCreated = MutableLiveData<Boolean>()
    val playlistCreated: LiveData<Boolean> get() = _playlistCreated

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
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "playlist_cover_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            return Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            _playlistCreated.value = false
            return null
        }
    }
}