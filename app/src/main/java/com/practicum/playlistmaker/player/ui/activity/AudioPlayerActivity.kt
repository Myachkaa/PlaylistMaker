package com.practicum.playlistmaker.player.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.player.domain.models.AudioPlayerState
import com.practicum.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private val viewModel: AudioPlayerViewModel by viewModel()
    private lateinit var binding: ActivityAudioPlayerBinding
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getParcelableExtra<Track>(KEY_TRACK)

        val url = track?.previewUrl

        viewModel.pState.observe(this) { state ->

            if (track != null) {
                binding.playerTrackName.text = track.trackName
                binding.playerArtistName.text = track.artistName
                binding.playerReleaseDateValue.text = track.releaseDate.substring(0, 4)
                binding.playerPrimaryGenreNameValue.text = track.primaryGenreName
                binding.playerCountryValue.text = track.country
                binding.playerTrackTimeValue.text = dateFormat.format(track.trackTime)
                binding.playerTime.text = dateFormat.format(state.progress)
                binding.playButton.isEnabled = state.isPlayButtonEnabled


                Glide.with(this)
                    .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .transform(
                        RoundedCorners(
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                8F,
                                resources.displayMetrics
                            ).toInt()
                        )
                    )
                    .into(binding.playerTrackArtwork)

                val isCollectionNameVisible = viewModel.getCollectionNameVisibility(track)
                binding.playerCollectionName.isVisible = isCollectionNameVisible
                binding.playerCollectionNameValue.isVisible = isCollectionNameVisible

                if (isCollectionNameVisible) {
                    binding.playerCollectionNameValue.text = track.collectionName
                }

                when (state) {
                    is AudioPlayerState.Playing -> binding.playButton.setImageResource(R.drawable.pause_button)

                    else -> binding.playButton.setImageResource(R.drawable.play_button)
                }
            }
        }

        viewModel.preparePlayer(url)

        binding.playerBackArrow.setOnClickListener { finish() }

        binding.playButton.setOnClickListener { playbackControl() }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    private fun playbackControl() {
        viewModel.playbackControl()
    }

    companion object {
        private const val KEY_TRACK = "track"
    }
}
