package com.practicum.playlistmaker.library.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.library.ui.view_model.FavoritesFragmentViewModel
import com.practicum.playlistmaker.library.ui.view_model.FavoritesState
import com.practicum.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.activity.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    companion object {
        fun newInstance() = FavoritesFragment()
        const val KEY_TRACK = "track"

    }

    private val viewModel by viewModel<FavoritesFragmentViewModel>()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val adapter = TrackAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            setState(it)
        }
        binding.recyclerViewFavorites.adapter = adapter
        binding.recyclerViewFavorites.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter.onItemClick = { track -> onTrackClicked(track) }
    }

    private fun onTrackClicked(track: Track) {
        if (viewModel.clickDebounce()) {
            val audioPlayerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
            audioPlayerIntent.putExtra(KEY_TRACK, track)
            startActivity(audioPlayerIntent)
        }
    }

    private fun showNotFound() {
        binding.recyclerViewFavorites.isVisible = false
        binding.placeholderNotFound.isVisible = true
    }

    private fun showTrackList() {
        binding.placeholderNotFound.isVisible = false
        binding.recyclerViewFavorites.isVisible = true
    }

    private fun setState(state: FavoritesState) {
        when (state) {
            is FavoritesState.Empty -> showNotFound()
            is FavoritesState.Content -> {
                showTrackList()
                adapter.tracks = state.tracks
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

