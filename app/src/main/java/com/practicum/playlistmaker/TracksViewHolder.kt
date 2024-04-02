package com.practicum.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var imageViewArtwork: ImageView = itemView.findViewById(R.id.imageViewArtwork)
    private var textViewTrackName: TextView = itemView.findViewById(R.id.textViewTrackName)
    private var textViewArtistName: TextView = itemView.findViewById(R.id.textViewArtistName)
    private var textViewTrackTime: TextView = itemView.findViewById(R.id.textViewTrackTime)


    fun bind(track: Track) {
        textViewTrackName.text = track.trackName
        textViewArtistName.text = track.artistName
        textViewTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        textViewArtistName.requestLayout()

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                2F,
                itemView.context.resources.displayMetrics).toInt()))
            .into(imageViewArtwork)
    }
}