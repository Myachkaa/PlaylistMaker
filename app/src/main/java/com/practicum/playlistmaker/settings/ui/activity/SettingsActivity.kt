package com.practicum.playlistmaker.settings.ui.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(applicationContext as App)
        ).get(SettingsViewModel::class.java)

        val back = findViewById<ImageView>(R.id.back)
        val share = findViewById<FrameLayout>(R.id.share)
        val support = findViewById<FrameLayout>(R.id.support)
        val agreement = findViewById<FrameLayout>(R.id.agreement)
        val themeSwitcher = findViewById<Switch>(R.id.themeSwitcher)

        themeSwitcher.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        back.setOnClickListener { finish() }

        share.setOnClickListener {
            viewModel.shareApp()
        }

        support.setOnClickListener {
            viewModel.openSupport()
        }

        agreement.setOnClickListener {
            viewModel.openTerms()
        }
    }
}