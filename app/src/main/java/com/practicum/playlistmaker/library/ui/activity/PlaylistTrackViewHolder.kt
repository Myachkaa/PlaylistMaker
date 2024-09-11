package com.practicum.playlistmaker.library.ui.activity

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistLayoutBinding
import com.practicum.playlistmaker.library.domain.models.Playlist

class PlaylistTrackViewHolder(private val binding: PlaylistLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.textViewPlaylistName.text = playlist.name
        binding.textViewTrackCount.text = trackCount(playlist.trackCount)

        Glide.with(itemView)
            .load(playlist.coverImagePath)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                CenterCrop(),
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2F,
                        itemView.context.resources.displayMetrics
                    ).toInt()
                )
            )
            .into(binding.imageViewArtwork)
    }

    private fun trackCount(trackQty: Int): String {
        if (trackQty % 100 in 5..20) return "$trackQty треков"
        if (trackQty % 10 == 1) return "$trackQty трек"
        if (trackQty % 10 in 2..4) return "$trackQty трека"
        else return "$trackQty треков"
    }
}