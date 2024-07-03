package com.practicum.playlistmaker.library.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityLibraryBinding
import com.practicum.playlistmaker.library.ui.view_model.LibraryViewModel

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    private val viewModel: LibraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.libraryBackButton.setOnClickListener {
            finish()
        }

        binding.viewPager.adapter = LibraryPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorites)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}