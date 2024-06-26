package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(link: String, share: String)
    fun openLink(url: String)
    fun openEmail(emailData: EmailData)
    fun getSupportEmail(): String
    fun getSupportSubject(): String
    fun getSupportMessage(): String
    fun getShareAppLink(): String
    fun getShareAppText(): String
    fun getTermsLink(): String
}