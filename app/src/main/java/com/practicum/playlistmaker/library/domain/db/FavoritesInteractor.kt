package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addToFavorites(trackEntity: TrackEntity)
    fun convertToTrackEntity(track: Track): TrackEntity
    suspend fun deleteFromFavorites(trackEntity: TrackEntity)
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun getFavoriteTrack(trackId: Long): Int
}