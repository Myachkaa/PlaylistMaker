package com.practicum.playlistmaker.library.data.impl

import com.practicum.playlistmaker.library.data.converters.TrackDbConverter
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.library.domain.api.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConverter
) : FavoritesRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return appDatabase.trackDao().getFavoriteTracks()
            .map { entities ->
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
        appDatabase.trackDao().deleteFromFavorites(trackEntity)
    }

    override suspend fun addToFavorites(trackEntity: TrackEntity) {
        appDatabase.trackDao().addToFavorites(trackEntity)
    }
}