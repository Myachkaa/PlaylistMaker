package com.practicum.playlistmaker.library.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistGridLayoutBinding
import com.practicum.playlistmaker.library.domain.models.Playlist

class PlaylistAdapter(private val playlists: MutableList<Playlist>) :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    var onItemClick: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding =
            PlaylistGridLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun setPlaylists(playlists: List<Playlist>) {
        this.playlists.clear()
        this.playlists.addAll(playlists)
        notifyDataSetChanged()
    }
}
