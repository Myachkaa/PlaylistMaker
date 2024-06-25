package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(THEME_SWITCHER, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(SWITCHER_KEY, false)
        switchTheme(darkTheme)
    }


    fun switchTheme(darkThemeEnabled: Boolean) {
        saveThemeState(darkThemeEnabled)
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun saveThemeState(darkThemeEnabled: Boolean) {
        val sharedPrefs = getSharedPreferences(THEME_SWITCHER, MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean(SWITCHER_KEY, darkThemeEnabled)
            .apply()
    }


    companion object {
        const val THEME_SWITCHER = "practicum_playlistmaker_theme_switcher"
        const val SWITCHER_KEY = "key_for_theme_switcher"
    }
}