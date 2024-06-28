package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        AudioPlayerViewModel(get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }
}
