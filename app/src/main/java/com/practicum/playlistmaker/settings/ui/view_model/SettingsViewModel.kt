package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingInteractor: SettingsInteractor
) : ViewModel() {


    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun getThemeSettings(): ThemeSettings {
        return settingInteractor.getThemeSettings()
    }

    fun updateThemeSetting(settings: ThemeSettings, darkThemeEnabled: Boolean) {
        settingInteractor.updateThemeSetting(settings, darkThemeEnabled)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        settingInteractor.switchTheme(darkThemeEnabled)
    }
}