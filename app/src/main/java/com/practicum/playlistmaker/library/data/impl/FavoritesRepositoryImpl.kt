package com.practicum.playlistmaker.library.data.impl

import com.practicum.playlistmaker.library.data.converters.FavoritesTrackDbConverter
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.library.data.db.entity.FavoritesTrackEntity
import com.practicum.playlistmaker.library.domain.api.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: FavoritesTrackDbConverter
) : FavoritesRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return appDatabase.favoritesDao().getFavoriteTracks()
            .map { entities ->
                entities.map { trackDbConvertor.map(it) }
            }
    }

    override suspend fun getFavoriteTrack(trackId: Long): Int {
        return appDatabase.favoritesDao().getFavoriteTrack(trackId)
    }

    override fun convertToTrackEntity(track: Track): FavoritesTrackEntity {
        return trackDbConvertor.map(track)
    }

    override suspend fun deleteFromFavorites(favoritesTrackEntity: FavoritesTrackEntity) {
        appDatabase.favoritesDao().deleteFromFavorites(favoritesTrackEntity)
    }

    override suspend fun addToFavorites(favoritesTrackEntity: FavoritesTrackEntity) {
        appDatabase.favoritesDao().addToFavorites(favoritesTrackEntity)
    }
}