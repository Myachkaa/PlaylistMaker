package com.practicum.playlistmaker.library.domain.impl

import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.library.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.library.domain.api.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) :
    FavoritesInteractor {
    override suspend fun addToFavorites(trackEntity: TrackEntity) {
        return favoritesRepository.addToFavorites(trackEntity)
    }

    override fun convertToTrackEntity(track: Track): TrackEntity {
        return favoritesRepository.convertToTrackEntity(track)
    }

    override suspend fun deleteFromFavorites(trackEntity: TrackEntity) {
        return favoritesRepository.deleteFromFavorites(trackEntity)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoritesRepository.getFavoriteTracks()
    }

    override suspend fun getFavoriteTrack(trackId: Long): Int {
        return favoritesRepository.getFavoriteTrack(trackId)
    }

}