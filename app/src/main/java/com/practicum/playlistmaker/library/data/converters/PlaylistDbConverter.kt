package com.practicum.playlistmaker.library.data.converters

import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.domain.models.Playlist

class PlaylistDbConverter {
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverImagePath,
            playlist.trackIds,
            playlist.trackCount
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverImagePath,
            playlist.trackIds,
            playlist.trackCount
        )
    }
}