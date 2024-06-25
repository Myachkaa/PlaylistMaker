package com.practicum.playlistmaker.settings.data.impl

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(context: Context) : SettingsRepository {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(THEME_SWITCHER, Context.MODE_PRIVATE)

    override fun getThemeSettings(): ThemeSettings {
        val isNightMode = preferences.getBoolean(SWITCHER_KEY, false)
        return ThemeSettings(isNightMode)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        preferences.edit()
            .putBoolean("night_mode", settings.isNightMode)
            .apply()
    }

    companion object {
        const val THEME_SWITCHER = "playlistmaker_theme_switcher"
        const val SWITCHER_KEY = "key_for_theme_switcher"
    }

}