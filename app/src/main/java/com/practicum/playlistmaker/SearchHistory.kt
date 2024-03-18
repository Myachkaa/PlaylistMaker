package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPref: SharedPreferences) {

    private val gson = Gson()
    private val editor = sharedPref.edit()
    fun saveTrack(track: Track) {
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
    fun getSearchHistory(): List<Track> {
        val json = this.sharedPref.getString(SEARCH_HISTORY_KEY, null)
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
    fun clearSearchHistory() {
        editor.remove(SEARCH_HISTORY_KEY)
        editor.apply()
    }

    companion object{
        private const val MAX_HISTORY_SIZE = 10
        private const val SEARCH_HISTORY_KEY = "search_history"
    }
}