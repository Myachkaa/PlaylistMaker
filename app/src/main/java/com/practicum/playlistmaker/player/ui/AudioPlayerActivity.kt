package com.practicum.playlistmaker.player.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var playButton: ImageView
    private lateinit var url: String
    private lateinit var updateProgressRunnable: Runnable
    private lateinit var playerTime: TextView
    private lateinit var viewModel: AudioPlayerViewModel
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory()
        )[AudioPlayerViewModel::class.java]
        val trackJsonString = intent.getStringExtra(KEY_TRACK_JSON)
        viewModel.setTrack(trackJsonString)

        url = viewModel.pState.value?.track?.previewUrl!!

        val playerTrackArtwork = findViewById<ImageView>(R.id.playerTrackArtwork)
        val playerTrackName = findViewById<TextView>(R.id.playerTrackName)
        val playerArtistName = findViewById<TextView>(R.id.playerArtistName)
        val playerCollectionName = findViewById<TextView>(R.id.playerCollectionNameValue)
        val playerReleaseDate = findViewById<TextView>(R.id.playerReleaseDateValue)
        val playerPrimaryGenreName = findViewById<TextView>(R.id.playerPrimaryGenreNameValue)
        val playerCountry = findViewById<TextView>(R.id.playerCountryValue)
        val playerTrackTime = findViewById<TextView>(R.id.playerTrackTimeValue)
        val collectionNameTextView = findViewById<TextView>(R.id.playerCollectionName)
        val backButton = findViewById<ImageView>(R.id.playerBackArrow)
        playerTime = findViewById(R.id.playerTime)

        playButton = findViewById(R.id.playButton)

        viewModel.pState.observe(this) { state ->
            val track = state.track

            if (track != null) {
                playerTrackName.text = track.trackName
                playerArtistName.text = track.artistName
                playerReleaseDate.text = track.releaseDate!!.substring(0, 4)
                playerPrimaryGenreName.text = track.primaryGenreName
                playerCountry.text = track.country
                playerTrackTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)

                Glide.with(this)
                    .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
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
                    .into(playerTrackArtwork)

                val isCollectionNameVisible = viewModel.getCollectionNameVisibility(track)
                playerCollectionName.isVisible = isCollectionNameVisible
                collectionNameTextView.isVisible = isCollectionNameVisible

                if (isCollectionNameVisible) {
                    playerCollectionName.text = track.collectionName
                }
            }
            playButton.isEnabled = state.isPrepared
            playButton.setImageResource(if (state.isPlaying) R.drawable.pause_button else R.drawable.play_button)
            playerTime.text = dateFormat.format(state.currentPosition)
        }

        viewModel.preparePlayer(url)

        backButton.setOnClickListener { finish() }

        playButton.setOnClickListener { playbackControl() }


        updateProgressRunnable = object : Runnable {
            override fun run() {
                viewModel.updateCurrentPosition()
                handler.postDelayed(this, UPDATE_TIME)
            }
        }
        handler.post(updateProgressRunnable)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
        handler.removeCallbacks(updateProgressRunnable)
    }

    private fun playbackControl() {
        viewModel.playbackControl()
    }

    companion object {
        private const val KEY_TRACK_JSON = "trackJson"
        private const val UPDATE_TIME = 500L
    }
}
