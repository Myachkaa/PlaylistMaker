package com.practicum.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val preferences: SharedPreferences) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        val isNightMode = preferences.getBoolean(SWITCHER_KEY, false)
        return ThemeSettings(isNightMode)
    }

    override fun updateThemeSetting(settings: ThemeSettings, darkThemeEnabled: Boolean) {
        preferences.edit()
            .putBoolean(SWITCHER_KEY, settings.isNightMode)
            .apply()
        switchTheme(darkThemeEnabled)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val SWITCHER_KEY = "key_for_theme_switcher"
    }

}