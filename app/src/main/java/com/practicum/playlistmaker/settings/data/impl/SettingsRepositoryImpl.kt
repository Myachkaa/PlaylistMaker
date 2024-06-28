package com.practicum.playlistmaker.settings.data.impl

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val preferences: SharedPreferences) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        val isNightMode = preferences.getBoolean(SWITCHER_KEY, false)
        return ThemeSettings(isNightMode)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        preferences.edit()
            .putBoolean(SWITCHER_KEY, settings.isNightMode)
            .apply()
    }

    companion object {
        const val SWITCHER_KEY = "key_for_theme_switcher"
    }

}