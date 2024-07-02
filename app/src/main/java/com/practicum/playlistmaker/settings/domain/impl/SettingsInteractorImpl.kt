package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor {

    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings, darkThemeEnabled: Boolean) {
        settingsRepository.updateThemeSetting(settings, darkThemeEnabled)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        settingsRepository.switchTheme(darkThemeEnabled)
    }


}