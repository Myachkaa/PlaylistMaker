package com.practicum.playlistmaker.presentation.ui.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var playButton: ImageView
    private lateinit var url: String
    private lateinit var updateProgressRunnable: Runnable
    private lateinit var playerTime: TextView
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val audioPlayer = Creator.provideAudioPlayerInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val trackJsonString = intent.getStringExtra(KEY_TRACK_JSON)
        val track: Track = audioPlayer.getTrackFromJson(trackJsonString)

        url = track.previewUrl

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

        playerTrackName.text = track.trackName
        playerArtistName.text = track.artistName
        playerReleaseDate.text = track.releaseDate.substring(0, 4)
        playerPrimaryGenreName.text = track.primaryGenreName
        playerCountry.text = track.country
        playerCountry.text = track.country
        playerTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)

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

        audioPlayer.preparePlayer(url,
            {playButton.isEnabled = true},
            {playButton.setImageResource(R.drawable.play_button)
                playerTime.text = getString(R.string.player_time)})

        if (track.collectionName.isEmpty() || track.collectionName.contains("Single")) {
            playerCollectionName.isVisible = false
            collectionNameTextView.isVisible = false
        } else playerCollectionName.text = track.collectionName


        backButton.setOnClickListener { finish() }

        playButton.setOnClickListener { playbackControl() }


        updateProgressRunnable = object : Runnable {
            override fun run() {
                playerTime.post {
                    playerTime.text = dateFormat.format(audioPlayer.currentPosition())
                }
                handler.postDelayed(this, UPDATE_TIME)
            }
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.release()
        handler.removeCallbacks(updateProgressRunnable)
    }

    private fun startPlayer() {
        audioPlayer.startPlayer()
        playButton.setImageResource(R.drawable.pause_button)
        handler.post(updateProgressRunnable)
    }

    private fun pausePlayer() {
        audioPlayer.pausePlayer()
        playButton.setImageResource(R.drawable.play_button)
        handler.removeCallbacks(updateProgressRunnable)
    }



    private fun playbackControl() {
        when(audioPlayer.playbackControl()) {
            false -> {
                pausePlayer()
            }
            true -> {
                startPlayer()
            }
        }
    }
    companion object {
        private const val KEY_TRACK_JSON = "trackJson"
        private const val UPDATE_TIME = 500L
    }
}
//    private fun preparePlayer() {
//            mediaPlayer.setDataSource(url)
//            mediaPlayer.prepareAsync()
//            mediaPlayer.setOnPreparedListener {
//                playButton.isEnabled = true
//                playerState = STATE_PREPARED
//
//        }
//        mediaPlayer.setOnCompletionListener {
//            playButton.setImageResource(R.drawable.play_button)
//            playerState = STATE_PREPARED
//            playerTime.text = getString(R.string.player_time)
//        }
//    }

//    private fun playbackControl() {
//        when(playerState) {
//            STATE_PLAYING -> {
//                pausePlayer()
//            }
//            STATE_PREPARED, STATE_PAUSED -> {
//                startPlayer()
//            }
//        }
//    }