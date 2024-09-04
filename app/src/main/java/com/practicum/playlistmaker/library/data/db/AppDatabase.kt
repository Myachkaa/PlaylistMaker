package com.practicum.playlistmaker.library.data.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.library.data.db.dao.TrackDao
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.library.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.library.data.db.dao.PlaylistTrackDao
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.db.entity.PlaylistTrackEntity

@Database(
    version = 3,
    exportSchema = false,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao

}
