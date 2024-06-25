package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink(), getShareAppText())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return externalNavigator.getString(R.string.share_link)
    }

    private fun getShareAppText(): String {
        return externalNavigator.getString(R.string.share)
    }

    private fun getSupportEmailData(): EmailData {
        val supportEmail = externalNavigator.getString(R.string.support_mail)
        val subject = externalNavigator.getString(R.string.subject_mail)
        val body = externalNavigator.getString(R.string.message)
        return EmailData(supportEmail, subject, body)
    }

    private fun getTermsLink(): String {
        return externalNavigator.getString(R.string.agreement_link)
    }
}