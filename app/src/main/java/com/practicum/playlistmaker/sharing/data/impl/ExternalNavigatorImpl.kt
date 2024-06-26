package com.practicum.playlistmaker.sharing.data.impl

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val application: Application) : ExternalNavigator {

    override fun shareLink(link: String, share: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooserIntent = Intent.createChooser(shareIntent, share).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.startActivity(chooserIntent)
    }

    override fun openLink(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(browserIntent)
    }

    override fun openEmail(emailData: EmailData) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailData.message)
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(emailIntent)
    }

    private fun getString(resId: Int): String {
        return application.getString(resId)
    }

    override fun getSupportEmail(): String {
        return getString(R.string.support_mail)
    }

    override fun getSupportSubject(): String {
        return getString(R.string.subject_mail)
    }

    override fun getSupportMessage(): String {
        return getString(R.string.message)
    }

    override fun getShareAppLink(): String {
        return getString(R.string.share_link)
    }

    override fun getShareAppText(): String {
        return getString(R.string.share)
    }

    override fun getTermsLink(): String {
        return getString(R.string.agreement_link)
    }
}