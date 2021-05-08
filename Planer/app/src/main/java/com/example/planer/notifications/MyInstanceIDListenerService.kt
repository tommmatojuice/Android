package com.example.planer.notifications

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class MyInstanceIDListenerService: FirebaseInstanceIdService()
{
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("MyInstanceIDListener", "Refreshed token: $refreshedToken")
        refreshedToken?.let { sendRegistrationToServer(it) }
    }

    private fun sendRegistrationToServer(token: String) {
        // TODO: Implement this method to send token to your app server.
    }
}