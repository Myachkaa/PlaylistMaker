package com.practicum.playlistmaker.library.data.converters

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity

class TrackDbConverter {
    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            addedAt = System.currentTimeMillis()
        )
    }
}
