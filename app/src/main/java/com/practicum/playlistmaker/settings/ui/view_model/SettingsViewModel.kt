package com.practicum.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(private val sharingInteractor: SharingInteractor) : ViewModel() {
    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    companion object {
        fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val sharingInteractor = Creator.provideSharingInteractor(application)
                    SettingsViewModel(sharingInteractor)
                }
            }
    }
}