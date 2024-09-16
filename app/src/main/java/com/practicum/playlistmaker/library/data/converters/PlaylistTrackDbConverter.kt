package com.practicum.playlistmaker.library.data.converters

import com.practicum.playlistmaker.library.data.db.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.library.domain.models.PlaylistTrack

class PlaylistTrackDbConverter {
    fun map(playlistTrack: PlaylistTrackEntity): PlaylistTrack {
        return PlaylistTrack(
            playlistTrack.trackId,
            playlistTrack.playlistId
        )
    }

    fun map(playlistTrack: PlaylistTrack): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            playlistTrack.trackId,
            playlistTrack.playlistId,
            addedAt = System.currentTimeMillis()
        )
    }
}