package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.ui.view_model.CreatePlaylistViewModel
import com.practicum.playlistmaker.library.ui.view_model.FavoritesFragmentViewModel
import com.practicum.playlistmaker.library.ui.view_model.LibraryViewModel
import com.practicum.playlistmaker.library.ui.view_model.PlaylistsFragmentViewModel
import com.practicum.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(get())
    }
    viewModel<AudioPlayerViewModel> {
        AudioPlayerViewModel(get(), get(), get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }
    viewModel<LibraryViewModel> {
        LibraryViewModel()
    }
    viewModel<FavoritesFragmentViewModel> {
        FavoritesFragmentViewModel(get())
    }
    viewModel<PlaylistsFragmentViewModel> {
        PlaylistsFragmentViewModel(get())
    }
    viewModel {
        CreatePlaylistViewModel(get())
    }
}
