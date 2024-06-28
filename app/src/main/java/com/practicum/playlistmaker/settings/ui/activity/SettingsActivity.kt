package com.practicum.playlistmaker.settings.ui.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back = findViewById<ImageView>(R.id.back)
        val share = findViewById<FrameLayout>(R.id.share)
        val support = findViewById<FrameLayout>(R.id.support)
        val agreement = findViewById<FrameLayout>(R.id.agreement)
        val themeSwitcher = findViewById<Switch>(R.id.themeSwitcher)

        themeSwitcher.isChecked = viewModel.getThemeSettings().isNightMode

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.updateThemeSetting(ThemeSettings(checked), checked)
            viewModel.switchTheme(checked)
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