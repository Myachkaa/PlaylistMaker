package com.practicum.playlistmaker.library.ui.activity

import android.util.TypedValue
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.PlaylistGridLayoutBinding
import com.practicum.playlistmaker.library.domain.models.Playlist

class PlaylistViewHolder(private val binding: PlaylistGridLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.playlistName.text = playlist.name
        binding.tvPlaylistTrackQty.text = trackCount(playlist.trackCount)

        if (playlist.coverImagePath.isNullOrEmpty()) {
            binding.playlistCover.isVisible = false
            binding.placeholderImage.isVisible = true
        } else {
            binding.placeholderImage.isVisible = false
            binding.playlistCover.isVisible = true
            Glide.with(itemView.context)
                .load(playlist.coverImagePath)
                .transform(
                    CenterCrop(), RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            8F,
                            itemView.context.resources.displayMetrics
                        ).toInt()
                    )
                )
                .into(binding.playlistCover)
        }
    }

    private fun trackCount(trackQty: Int): String {
        if (trackQty % 100 in 5..20) return "$trackQty треков"
        if (trackQty % 10 == 1) return "$trackQty трек"
        if (trackQty % 10 in 2..4) return "$trackQty трека"
        else return "$trackQty треков"
    }
}