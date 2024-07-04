package com.practicum.playlistmaker.di

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
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
}
