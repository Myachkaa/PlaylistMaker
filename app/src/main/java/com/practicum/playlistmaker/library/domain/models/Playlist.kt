package com.practicum.playlistmaker.library.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Playlist(
    val id: Long,
    val name: String,
    val description: String?,
    val coverImagePath: String?,
    val trackIds: String,
    val trackCount: Int
) : Parcelable