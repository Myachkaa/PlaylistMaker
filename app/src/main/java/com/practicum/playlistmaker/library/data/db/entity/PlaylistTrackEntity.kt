package com.practicum.playlistmaker.library.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_tracks")
data class PlaylistTrackEntity(
    @PrimaryKey val trackId: Long,
    @ColumnInfo(name = "playlist_id") val playlistId: Long
)