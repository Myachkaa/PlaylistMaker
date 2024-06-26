package com.practicum.playlistmaker.search.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track

class TrackAdapter : RecyclerView.Adapter<TracksViewHolder>() {

    var tracks: List<Track> = ArrayList()
    var onItemClick: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val viewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TracksViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tracks[position])
        }
    }

    override fun getItemCount(): Int = tracks.size


}

