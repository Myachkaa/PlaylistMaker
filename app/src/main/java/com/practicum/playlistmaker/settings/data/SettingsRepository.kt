package com.practicum.playlistmaker.settings.data

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings, darkThemeEnabled: Boolean)
    fun switchTheme(darkThemeEnabled: Boolean)
}