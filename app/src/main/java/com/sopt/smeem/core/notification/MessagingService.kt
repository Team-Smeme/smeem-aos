package com.sopt.smeem.core.notification

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        // TODO : 디바이스 토큰이 새롭게 변경되었을때 작업
        // sendRegistrationToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        // TODO : 푸시를 받은 후처리 작업
    }
}