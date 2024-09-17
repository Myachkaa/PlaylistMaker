package com.practicum.playlistmaker.library.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistViewingBinding
import com.practicum.playlistmaker.library.domain.models.Playlist
import com.practicum.playlistmaker.library.ui.view_model.PlaylistViewingViewModel
import com.practicum.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.activity.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistViewingFragment : Fragment() {

    private var _binding: FragmentPlaylistViewingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewingViewModel by viewModel()
    private val trackAdapter = TrackAdapter()
    private val args: PlaylistViewingFragmentArgs by navArgs()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistViewingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getLong(PLAYLIST_ID) ?: return
        viewModel.loadPlaylist(playlistId)

        setupUI()
        setupObservers()
        binding.playlistRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            PLAYLIST_UPDATED
        )
            ?.observe(viewLifecycleOwner) { updated ->
                if (updated) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetOption)
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_HIDDEN

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                        else -> binding.overlay.isVisible = true
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }
    }

    private fun setupUI() {
        trackAdapter.onItemClick = { track ->
            onTrackClicked(track)
        }
        trackAdapter.onItemLongClick = { track ->
            showDeleteTrackDialog(track)
        }
        binding.playerBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.playlistShare.setOnClickListener { handleSharePlaylist() }
        binding.playlistOption.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.sharePlaylist.setOnClickListener { handleSharePlaylist() }

        binding.editPlaylist.setOnClickListener {
            val playlist = viewModel.playlist.value
            playlist?.let {
                navigateToEditPlaylist(playlist.id)
            }
        }

        binding.deletePlaylist.setOnClickListener { showDeletePlaylistDialog() }
    }

    private fun onTrackClicked(track: Track) {
        if (viewModel.clickDebounce()) {
            val audioPlayerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
            audioPlayerIntent.putExtra(FavoritesFragment.KEY_TRACK, track)
            startActivity(audioPlayerIntent)
        }
    }

    private fun setupObservers() {
        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            if (playlist != null) {
                bindPlaylistData(playlist)
            }
        }

        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            trackAdapter.updateTracks(tracks)
            updatePlaylistStats(tracks)
            handleEmptyState(tracks.isEmpty())
        }
    }

    private fun handleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.playlistRecyclerView.isVisible = false
            binding.emptyMessage.isVisible = true
        } else {
            binding.playlistRecyclerView.isVisible = true
            binding.emptyMessage.isVisible = false
        }
    }

    private fun bindPlaylistData(playlist: Playlist) {
        binding.playlistName.text = playlist.name
        binding.playlistDescription.text = playlist.description
        binding.imageViewArtwork
        binding.textViewPlaylistName.text = playlist.name
        binding.textViewTrackCount.text = resources.getQuantityString(
            R.plurals.track_count,
            playlist.trackCount,
            playlist.trackCount
        )

        Glide.with(this)
            .load(playlist.coverImagePath)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                CenterCrop(),
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2F,
                        resources.displayMetrics
                    ).toInt()
                )
            )
            .into(binding.imageViewArtwork)

        if (playlist.coverImagePath.isNullOrEmpty()) {
            binding.playlistCover.isVisible = false
            binding.placeholderImage.isVisible = true
        } else {
            binding.playlistCover.isVisible = true
            binding.placeholderImage.isVisible = false
            Glide.with(this)
                .load(playlist.coverImagePath)
                .error(R.drawable.placeholder)
                .into(binding.playlistCover)
        }
    }

    private fun showDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_track)
            .setMessage(R.string.delete_message)
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.delete) { dialog, _ ->
                viewModel.deleteTrackFromPlaylist(track.trackId, args.playlistId)
                dialog.dismiss()
            }
            .show()
    }

    private fun showDeletePlaylistDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_playlist)
            .setMessage(R.string.delete_playlist_message)
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.delete) { dialog, _ ->
                deletePlaylist()
                dialog.dismiss()
            }
            .show()
    }

    private fun updatePlaylistStats(tracks: List<Track>) {
        val totalDurationInMillis = tracks.sumOf { it.trackTime }
        val totalDurationInMinutes = totalDurationInMillis / 60000

        binding.playlistDuration.text = resources.getQuantityString(
            R.plurals.minute_count,
            totalDurationInMinutes.toInt(),
            totalDurationInMinutes
        )
        binding.quantity.text =
            resources.getQuantityString(R.plurals.track_count, tracks.size, tracks.size)
    }

    private fun showCustomToast(message: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast, null)

        val text: TextView = layout.findViewById(R.id.toast_text)
        text.text = message

        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 0)
        toast.show()
    }

    private fun handleSharePlaylist() {
        if (trackAdapter.itemCount == 0) {
            showCustomToast(getString(R.string.empty_playlist_message))
        } else {
            val shareText = viewModel.createShareText(viewModel.playlist.value)
            sharePlaylist(shareText)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun sharePlaylist(shareText: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun deletePlaylist() {
        viewModel.deletePlaylist(args.playlistId)
        findNavController().popBackStack()
    }

    private fun navigateToEditPlaylist(playlistId: Long) {
        val bundle = Bundle().apply {
            putLong(PLAYLIST_ID, playlistId)
        }
        findNavController().navigate(R.id.editPlaylistFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PLAYLIST_ID = "playlistId"
        private const val PLAYLIST_UPDATED = "playlist_updated"
    }
}