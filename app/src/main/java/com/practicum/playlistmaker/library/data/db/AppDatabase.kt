package com.practicum.playlistmaker.library.data.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.library.data.db.dao.FavoritesTrackDao
import com.practicum.playlistmaker.library.data.db.entity.FavoritesTrackEntity
import com.practicum.playlistmaker.library.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.library.data.db.dao.PlaylistTrackDao
import com.practicum.playlistmaker.library.data.db.dao.TrackDao
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.db.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity

@Database(
    version = 8,
    exportSchema = false,
    entities = [FavoritesTrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class, TrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesTrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao
    abstract fun trackDao(): TrackDao

}
