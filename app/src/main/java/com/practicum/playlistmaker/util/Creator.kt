package com.practicum.playlistmaker.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    private fun getHistoryRepository(sharedPref: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPref)
    }

    fun provideTrackInteractor(context: Context, sharedPref: SharedPreferences): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context), getHistoryRepository(sharedPref))
    }

    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }

    fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    private fun getExternalNavigator(application: Application): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    fun provideSharingInteractor(application: Application): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(application))
    }
}