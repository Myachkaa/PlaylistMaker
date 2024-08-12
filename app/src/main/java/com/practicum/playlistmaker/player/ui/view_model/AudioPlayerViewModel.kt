package com.practicum.playlistmaker.player.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.db.FavoritesInteractor
import com.practicum.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.models.AudioPlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AudioPlayerViewModel(
    private val interactor: AudioPlayerInteractor,
    private val dbInteractor: FavoritesInteractor
) : ViewModel() {

    private val _playerState = MutableLiveData<AudioPlayerState>(AudioPlayerState.Default())
    val playerState: LiveData<AudioPlayerState> get() = _playerState

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private var timerJob: Job? = null

    fun preparePlayer(url: String?) {
        if (url != null) {
            viewModelScope.launch {
                interactor.preparePlayer(url, {
                    _playerState.value = AudioPlayerState.Prepared()
                }, {
                    _playerState.value = AudioPlayerState.Prepared()
                })
            }
        }
    }

    private fun startPlayer() {
        viewModelScope.launch {
            interactor.startPlayer()
            updateCurrentPosition()
        }
    }


    fun pausePlayer() {
        viewModelScope.launch {
            timerJob?.cancel()
            interactor.pausePlayer()
        }
    }

    fun playbackControl() {
        when (_playerState.value) {
            is AudioPlayerState.Playing -> {
                pausePlayer()
                _playerState.postValue(AudioPlayerState.Paused(interactor.currentPosition()))
            }

            is AudioPlayerState.Prepared, is AudioPlayerState.Paused -> {
                startPlayer()
                _playerState.postValue(AudioPlayerState.Playing(interactor.currentPosition()))
            }

            else -> {
                _playerState.postValue(AudioPlayerState.Prepared())
            }
        }
    }

    private fun updateCurrentPosition() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(UPDATE_TIME)
                if (_playerState.value is AudioPlayerState.Playing) {
                    _playerState.postValue(AudioPlayerState.Playing(interactor.currentPosition()))
                }
            }
        }
    }

    fun release() {
        viewModelScope.launch {
            interactor.release()
            _playerState.value = AudioPlayerState.Default()
        }
    }

    fun getCollectionNameVisibility(track: Track): Boolean {
        return !(track.collectionName.isEmpty() || track.collectionName.contains("Single"))
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            val isCurrentlyFavorite = isTrackFavorite(track.trackId)
            if (isCurrentlyFavorite) {
                dbInteractor.deleteFromFavorites(dbInteractor.convertToTrackEntity(track))
            } else {
                dbInteractor.addToFavorites(dbInteractor.convertToTrackEntity(track))
            }
            track.isFavorite = !track.isFavorite
            _isFavorite.postValue(track.isFavorite)
        }
    }

    suspend fun isTrackFavorite(trackId: Long): Boolean {
        return dbInteractor.getFavoriteTrack(trackId) > 0
    }

    fun setIsFavorite(isFavorite: Boolean) {
        _isFavorite.postValue(isFavorite)
    }

    companion object {
        private const val UPDATE_TIME = 300L
    }
}