package com.practicum.playlistmaker.search.ui.activity

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackLayoutBinding
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(private val binding: TrackLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.textViewTrackName.text = track.trackName
        binding.textViewArtistName.text = track.artistName
        binding.textViewTrackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        binding.textViewArtistName.requestLayout()

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
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
}