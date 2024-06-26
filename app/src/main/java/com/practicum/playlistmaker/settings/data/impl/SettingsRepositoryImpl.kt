package com.practicum.playlistmaker.settings.data.impl

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(application: Application) : SettingsRepository {
    private val preferences: SharedPreferences =
        application.getSharedPreferences(THEME_SWITCHER, Context.MODE_PRIVATE)
    private val app = application as App

    override fun getThemeSettings(): ThemeSettings {
        val isNightMode = preferences.getBoolean(SWITCHER_KEY, false)
        return ThemeSettings(isNightMode)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        preferences.edit()
            .putBoolean(SWITCHER_KEY, settings.isNightMode)
            .apply()
        app.switchTheme(settings.isNightMode)
    }

    companion object {
        const val THEME_SWITCHER = "playlistmaker_theme_switcher"
        const val SWITCHER_KEY = "key_for_theme_switcher"
    }

}