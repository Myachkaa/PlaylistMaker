package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.library.data.converters.PlaylistTrackDbConverter
import com.practicum.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.library.data.converters.FavoritesTrackDbConverter
import com.practicum.playlistmaker.library.data.converters.TrackDbConverter
import com.practicum.playlistmaker.library.data.impl.CreatePlaylistRepositoryImpl
import com.practicum.playlistmaker.library.data.impl.FavoritesRepositoryImpl
import com.practicum.playlistmaker.library.data.impl.PlaylistRepositoryImpl
import com.practicum.playlistmaker.library.data.impl.PlaylistViewingRepositoryImpl
import com.practicum.playlistmaker.library.domain.api.CreatePlaylistRepository
import com.practicum.playlistmaker.library.domain.api.FavoritesRepository
import com.practicum.playlistmaker.library.domain.api.PlaylistRepository
import com.practicum.playlistmaker.library.domain.api.PlaylistViewingRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    factory { FavoritesTrackDbConverter() }
    factory { PlaylistDbConverter() }
    factory { PlaylistTrackDbConverter() }
    factory { TrackDbConverter() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<CreatePlaylistRepository> {
        CreatePlaylistRepositoryImpl(get(), get())
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get(), get())
    }
    single<PlaylistViewingRepository> {
        PlaylistViewingRepositoryImpl(get(), get(), get())
    }
}
