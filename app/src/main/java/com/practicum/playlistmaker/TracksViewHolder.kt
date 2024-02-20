package com.practicum.playlistmaker

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var imageViewArtwork: ImageView = itemView.findViewById(R.id.imageViewArtwork)
    private var textViewTrackName: TextView = itemView.findViewById(R.id.textViewTrackName)
    private var textViewArtistName: TextView = itemView.findViewById(R.id.textViewArtistName)
    private var textViewTrackTime: TextView = itemView.findViewById(R.id.textViewTrackTime)

    @RequiresApi(Build.VERSION_CODES.S)
    fun bind(track: Track) {
        textViewTrackName.text = track.trackName
        textViewArtistName.text = track.artistName
        textViewTrackTime.text = track.trackTime

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(imageViewArtwork)
    }
}