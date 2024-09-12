package com.practicum.playlistmaker.library.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistLayoutBinding
import com.practicum.playlistmaker.library.domain.models.Playlist

class PlaylistTrackAdapter : RecyclerView.Adapter<PlaylistTrackViewHolder>() {

    var playlists: List<Playlist> = ArrayList()
    var onItemClick: ((Playlist) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
        return PlaylistTrackViewHolder(PlaylistLayoutBinding.inflate(viewHolder, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size
    fun updatePlaylists(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}