package com.practicum.playlistmaker.search.ui.view_model

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.gson.Gson
import com.practicum.playlistmaker.player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.activity.SearchActivity

import com.practicum.playlistmaker.util.SearchResult

class SearchViewModel(private val trackInteractor: TrackInteractor) : ViewModel() {

    private var isClickAllowed = true
    private val tracks = ArrayList<Track>()
    var searchText: String = TEXT_DEF
    private var lastTrack: String? = null
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest(searchText) }

    fun searchRequest(track: String) {
        stateLiveData.postValue(SearchState.Loading)
        trackInteractor.searchTrack(track) { foundTrack ->
            handler.post {
                when (foundTrack) {
                    is SearchResult.Success -> {
                        val tracksResult = foundTrack.data ?: emptyList()
                        if (tracksResult.isEmpty()) {
                            stateLiveData.postValue(SearchState.NotFound)
                        } else {
                            tracks.clear()
                            tracks.addAll(tracksResult)
                            stateLiveData.postValue(SearchState.TrackList(tracks))
                        }
                    }

                    is SearchResult.Error -> {
                        when (foundTrack.code) {
                            -1 -> {
                                stateLiveData.postValue(SearchState.ServerError)
                                lastTrack = track
                            }

                            else -> stateLiveData.postValue(SearchState.NotFound)
                        }
                    }
                }
            }
        }
    }

    fun toJson(track: Track): String {
        return trackInteractor.toJson(track)
    }

    fun retryLastSearch() {
        lastTrack?.let {
            searchRequest(it)
        }
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(Any())
    }

    fun clearTracks() {
        tracks.clear()
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun clearSearchHistory() {
        trackInteractor.clearSearchHistory()
    }

    fun getSearchHistory(): List<Track> {
        return trackInteractor.getSearchHistory()
    }

    fun saveTrack(track: Track) {
        trackInteractor.saveTrack(track)
    }

    fun onSearchTextChanged(text: String) {
        searchText = text
        searchDebounce()
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        if (searchText.isNotEmpty()) {
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    companion object {
        const val TEXT_DEF = ""
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L

    }
}