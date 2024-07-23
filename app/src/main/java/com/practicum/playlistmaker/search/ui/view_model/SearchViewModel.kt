package com.practicum.playlistmaker.search.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val trackInteractor: TrackInteractor) : ViewModel() {

    private var isClickAllowed = true
    private val tracks = ArrayList<Track>()
    var searchText: String = TEXT_DEF
    private var lastTrack: String? = null
    private val stateLiveData = MutableLiveData<SearchState>()
    private var searchJob: Job? = null
    fun observeState(): LiveData<SearchState> = stateLiveData

    fun searchRequest(track: String) {
        stateLiveData.postValue(SearchState.Loading)
        viewModelScope.launch {
            trackInteractor.searchTrack(track)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
            lastTrack = track
        }
    }

    private fun processResult(foundTrack: List<Track>?, errorCode: Int?) {

        val tracksResult = mutableListOf<Track>()
        if (foundTrack != null) {
            tracksResult.addAll(foundTrack)
        }

        when (errorCode) {
            null -> {
                if (tracksResult.isEmpty()) {
                    stateLiveData.postValue(SearchState.NotFound)
                } else {
                    tracks.clear()
                    tracks.addAll(tracksResult)
                    stateLiveData.postValue(SearchState.TrackList(tracks))
                }
            }

            -1 -> {
                stateLiveData.postValue(SearchState.ServerError)
            }

            else -> stateLiveData.postValue(SearchState.NotFound)
        }
    }

    fun retryLastSearch() {
        lastTrack?.let {
            searchRequest(it)
        }
    }


    fun clearTracks() {
        tracks.clear()
    }

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
        searchJob?.cancel()
        if (searchText.isNotEmpty()) {
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchRequest(searchText)
            }
        }
    }

    companion object {
        const val TEXT_DEF = ""
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L

    }
}