package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

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

        val trackJson = intent.getStringExtra("trackJson")
        val gson = Gson()
        val track = gson.fromJson(trackJson, Track::class.java)

        playerTrackName.text = track.trackName
        playerArtistName.text = track.artistName
        playerReleaseDate.text = track.releaseDate.substring(0, 4)
        playerPrimaryGenreName.text = track.primaryGenreName
        playerCountry.text = track.country
        playerTrackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)

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
            .into(playerTrackArtwork)

        if (track.collectionName.isEmpty() || track.collectionName.contains("Single")) {
            playerCollectionName.isVisible = false
            collectionNameTextView.isVisible = false
        } else playerCollectionName.text = track.collectionName

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        val preferences: SharedPreferences =
            getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(PREF_CURRENT_SCREEN, SCREEN_AUDIO_PLAYER)
        editor.apply()
    }
    companion object {
        private const val PREF_CURRENT_SCREEN = "current_screen"
        private const val SCREEN_AUDIO_PLAYER = "audio_player"
        private const val PREF_NAME = "my_preferences"
    }
}