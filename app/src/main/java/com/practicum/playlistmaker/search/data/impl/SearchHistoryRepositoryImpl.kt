package com.practicum.playlistmaker.search.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class SearchHistoryRepositoryImpl(
    private val sharedPref: SharedPreferences,
    private val gson: Gson,
    private val appDatabase: AppDatabase
) : SearchHistoryRepository {


    private val editor = sharedPref.edit()

    override fun saveTrack(track: Track) {
        val history = getSearchHistory().toMutableList()

        if (history.contains(track)) {
            history.remove(track)
        }
        history.add(0, track)

        if (history.size > MAX_HISTORY_SIZE) {
            history.removeLast()
        }
        val json = gson.toJson(history)
        editor.putString(SEARCH_HISTORY_KEY, json)
        editor.apply()
    }

    override fun getSearchHistory(): List<Track> {
        val json = sharedPref.getString(SEARCH_HISTORY_KEY, null)
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        val history = gson.fromJson<ArrayList<Track>>(json, type) ?: arrayListOf()

        val favoriteTrackIds = runBlocking {
            appDatabase.trackDao().getFavoriteTracks().first().map { it.trackId }.toSet()
        }
        return history.map { track ->
            track.copy(isFavorite = favoriteTrackIds.contains(track.trackId))
        }
    }

    override fun clearSearchHistory() {
        editor.remove(SEARCH_HISTORY_KEY)
        editor.apply()
    }

    companion object {
        private const val MAX_HISTORY_SIZE = 10
        private const val SEARCH_HISTORY_KEY = "search_history"
    }
}