package com.practicum.playlistmaker.player.ui.view_model

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.search.domain.models.Track


class AudioPlayerViewModel(private val interactor: AudioPlayerInteractor) : ViewModel() {

    private val playerState = MutableLiveData<PlayerState>()
    val pState: LiveData<PlayerState> get() = playerState

    init {
        playerState.value = PlayerState()
    }

    fun setTrack(trackJsonString: String?) {
        playerState.value = playerState.value?.copy(
            track = interactor.getTrackFromJson(trackJsonString)
        )
    }

    fun preparePlayer(url: String?) {
        if (url != null) {
            interactor.preparePlayer(url, {
                playerState.value = playerState.value?.copy(isPrepared = true)
            }, {
                playerState.value =
                    playerState.value?.copy(
                        isPlaying = false,
                        isPrepared = true,
                        currentPosition = 0
                    )
            })
        }
    }


    fun pausePlayer() {
        interactor.pausePlayer()
        playerState.value = playerState.value?.copy(isPlaying = false)
    }

    fun playbackControl() {
        val isPlaying = interactor.playbackControl()
        playerState.value = playerState.value?.copy(isPlaying = isPlaying)
    }

    fun updateCurrentPosition() {
        playerState.value = playerState.value?.copy(currentPosition = interactor.currentPosition())
    }

    fun release() {
        interactor.release()
        playerState.value =
            playerState.value?.copy(isPlaying = false, isPrepared = false, currentPosition = 0)
    }

    fun getCollectionNameVisibility(track: Track): Boolean {
        return !(track.collectionName.isEmpty() || track.collectionName.contains("Single"))
    }
    companion object {
        private const val TAG = "AudioPlayerViewModel"
    }
}