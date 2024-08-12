package com.practicum.playlistmaker.library.data.impl

import android.util.Log
import com.practicum.playlistmaker.library.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.library.domain.db.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoritesRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return appDatabase.trackDao().getFavoriteTracks()
            .map { entities ->
                Log.d("FavoritesRepository", "Fetched ${entities.size} tracks from database")
                entities.map { trackDbConvertor.map(it) }
            }
    }

    override suspend fun getFavoriteTrack(trackId: Long): Int {
        return appDatabase.trackDao().getFavoriteTrack(trackId)
    }

    override fun convertToTrackEntity(track: Track): TrackEntity {
        return trackDbConvertor.map(track)
    }

    override suspend fun deleteFromFavorites(trackEntity: TrackEntity) {
        Log.d("FavoritesRepository", "Removing track to favorites: ${trackEntity.trackId}")
        appDatabase.trackDao().deleteFromFavorites(trackEntity)
    }

    override suspend fun addToFavorites(trackEntity: TrackEntity) {
        Log.d("FavoritesRepository", "Adding track to favorites: ${trackEntity.trackId}")
        appDatabase.trackDao().addToFavorites(trackEntity)
    }
}