package com.practicum.playlistmaker


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back = findViewById<ImageView>(R.id.back)
        val share = findViewById<FrameLayout>(R.id.share)
        val support = findViewById<FrameLayout>(R.id.support)
        val agreement = findViewById<FrameLayout>(R.id.agreement)
        val themeSwitcher = findViewById<Switch>(R.id.themeSwitcher)

        themeSwitcher.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        back.setOnClickListener {
            finish()
        }
        share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            shareIntent.type = "text/plain"
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        }
        support.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_mail)))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_mail))
            startActivity(supportIntent)
        }
        agreement.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_link)))
            startActivity(agreementIntent)
        }
    }
}