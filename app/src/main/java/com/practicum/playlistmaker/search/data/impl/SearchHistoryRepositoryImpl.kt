package com.practicum.playlistmaker.search.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track


class SearchHistoryRepositoryImpl(private val sharedPref: SharedPreferences) :
    SearchHistoryRepository {


    private val gson = Gson()
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
        return gson.fromJson(json, type) ?: emptyList()
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