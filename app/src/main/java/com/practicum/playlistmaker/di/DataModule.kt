package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.network.ITunsApi
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.library.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASEURL = "https://itunes.apple.com"
private const val SEARCH_HISTORY = "search_history"
private const val THEME_PREFS = "practicum_playlistmaker_theme_switcher"

val dataModule = module {

    single<ITunsApi> {
        Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunsApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    single(named("Theme_prefs")) {
        androidContext().getSharedPreferences(
            THEME_PREFS,
            Context.MODE_PRIVATE
        )
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

}
