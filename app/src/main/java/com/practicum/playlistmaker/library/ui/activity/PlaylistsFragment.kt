package com.practicum.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.library.ui.view_model.PlaylistsFragmentViewModel

class PlaylistsFragment : Fragment() {
    companion object {
        fun newInstance() = PlaylistsFragment()

    }

    private val viewModel: PlaylistsFragmentViewModel by viewModel()
    private lateinit var playlistAdapter: PlaylistAdapter

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistAdapter = PlaylistAdapter(mutableListOf())
        playlistAdapter.onItemClick = { playlist ->
            val action =
                LibraryFragmentDirections.actionLibraryFragmentToPlaylistViewingFragment(playlist.id)
            findNavController().navigate(action)
        }
        binding.playlist.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = playlistAdapter
        }

        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            if (playlists.isNotEmpty()) {
                binding.playlist.isVisible = true
                binding.placeholderNotFound.isVisible = false
                playlistAdapter.setPlaylists(playlists)
            } else {
                binding.playlist.isVisible = false
                binding.placeholderNotFound.isVisible = true
            }
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.createPlaylistFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
