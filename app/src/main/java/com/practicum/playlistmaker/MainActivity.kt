package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search = findViewById<Button>(R.id.search)
        val library = findViewById<Button>(R.id.library)
        val setting = findViewById<Button>(R.id.setting)

        val searchIntent = Intent(this, SearchActivity::class.java)

        val searchClickListener: View.OnClickListener = object : View.OnClickListener {

            override fun onClick(v: View?) {
                startActivity(searchIntent)
            }
        }
        search.setOnClickListener(searchClickListener)

        library.setOnClickListener {
            val libraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }

        setting.setOnClickListener {
            val settingIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingIntent)
        }
    }
}