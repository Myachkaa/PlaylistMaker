package com.practicum.playlistmaker

import android.media.MediaPlayer
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
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var playButton: ImageView
    private lateinit var url: String
    private lateinit var updateProgressRunnable: Runnable
    private lateinit var playerTime: TextView
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val trackJsonString = intent.getStringExtra(KEY_TRACK_JSON)
        val track: Track = Gson().fromJson(trackJsonString, Track::class.java)

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

        preparePlayer()

        if (track.collectionName.isEmpty() || track.collectionName.contains("Single")) {
            playerCollectionName.isVisible = false
            collectionNameTextView.isVisible = false
        } else playerCollectionName.text = track.collectionName


        backButton.setOnClickListener { finish() }

        playButton.setOnClickListener { playbackControl() }


        updateProgressRunnable = object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    playerTime.text = dateFormat.format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, UPDATE_TIME)
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateProgressRunnable)
    }

    private fun preparePlayer() {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playButton.isEnabled = true
                playerState = STATE_PREPARED

        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play_button)
            playerState = STATE_PREPARED
            playerTime.text = getString(R.string.player_time)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
        handler.post(updateProgressRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
        handler.removeCallbacks(updateProgressRunnable)
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    companion object {
        private const val KEY_TRACK_JSON = "trackJson"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val UPDATE_TIME = 500L
    }
}