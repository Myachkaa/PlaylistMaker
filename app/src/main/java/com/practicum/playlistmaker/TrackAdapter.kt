package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter : RecyclerView.Adapter<TracksViewHolder> () {

    var tracks = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder = TracksViewHolder(parent)

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks.get(position))
    }

    override fun getItemCount(): Int = tracks.size


}