package com.practicum.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.domain.models.Playlist
import com.practicum.playlistmaker.library.ui.view_model.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditPlaylistFragment : CreatePlaylistFragment() {

    private val editPlaylistViewModel: EditPlaylistViewModel by viewModel()

    private lateinit var playlist: Playlist

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createButton.text = getString(R.string.save)
        binding.screenTitle.text = getString(R.string.edit_playlist)

        arguments?.getParcelable<Playlist>(EXTRA_PLAYLIST)?.let {
            playlist = it
            editPlaylistViewModel.setPlaylistForEditing(playlist)
        }
        editPlaylistViewModel.name.observe(viewLifecycleOwner) { name ->
            binding.nameEditText.setText(name)
        }

        editPlaylistViewModel.description.observe(viewLifecycleOwner) { description ->
            binding.descriptionEditText.setText(description)
        }

        editPlaylistViewModel.coverImageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                Glide.with(this)
                    .load(it)
                    .apply(RequestOptions().override(1000, 1000))
                    .transform(CenterCrop(), RoundedCorners(8))
                    .into(binding.placeForCover)
            }
        }
        binding.createButton.setOnClickListener {
            if (binding.nameEditText.text?.isNotEmpty() == true) {
                val coverUriToSave = selectedImageUri ?: playlist.coverImagePath
                editPlaylistViewModel.savePlaylist(
                    binding.nameEditText.text.toString(),
                    binding.descriptionEditText.text.toString(),
                    coverUriToSave.toString()
                )
            }
        }

        editPlaylistViewModel.playlistCreated.observe(viewLifecycleOwner) { success ->
            if (success) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        binding.playerTrackArtwork.isVisible =
            editPlaylistViewModel.coverImageUri.value?.toString().isNullOrEmpty()
    }

    override fun handleBackNavigation() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    companion object {
        private const val EXTRA_PLAYLIST = "playlist"
        fun newInstance(playlist: Playlist): EditPlaylistFragment {
            val fragment = EditPlaylistFragment()
            val args = Bundle()
            args.putParcelable(EXTRA_PLAYLIST, playlist)
            fragment.arguments = args
            return fragment
        }
    }
}