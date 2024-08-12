package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.network.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksResponse
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.util.SearchResult
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) :
    TrackRepository {

    override fun searchTrack(expression: String): Flow<SearchResult<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            SUCCESS_CODE -> {
                val favoriteTrackIds =
                    appDatabase.trackDao().getFavoriteTracks().first().map { it.trackId }.toSet()
                emit(SearchResult.Success((response as TracksResponse).tracks.map {
                    Track(
                        it.trackId,
                        it.trackName ?: "",
                        it.artistName ?: "",
                        it.trackTime ?: 0L,
                        it.artworkUrl100 ?: "",
                        it.collectionName ?: "",
                        it.releaseDate ?: "",
                        it.primaryGenreName ?: "",
                        it.country ?: "",
                        it.previewUrl ?: "",
                        isFavorite = favoriteTrackIds.contains(it.trackId)
                    )
                }))

            }

            NETWORK_ERROR_CODE -> {
                emit(SearchResult.Error(NETWORK_ERROR_CODE))
            }

            else -> {
                emit(SearchResult.Error(GENERAL_ERROR_CODE))
            }
        }
    }
    companion object {
        private const val SUCCESS_CODE = 200
        private const val NETWORK_ERROR_CODE = -1
        private const val GENERAL_ERROR_CODE = 0
    }
}