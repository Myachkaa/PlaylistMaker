package com.practicum.playlistmaker.sharing.domain.impl

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
        return externalNavigator.getShareAppLink()
    }

    private fun getShareAppText(): String {
        return externalNavigator.getShareAppText()
    }

    private fun getSupportEmailData(): EmailData {
        val supportEmail = externalNavigator.getSupportEmail()
        val subject = externalNavigator.getSupportSubject()
        val body = externalNavigator.getSupportMessage()
        return EmailData(supportEmail, subject, body)
    }

    private fun getTermsLink(): String {
        return externalNavigator.getTermsLink()
    }
}