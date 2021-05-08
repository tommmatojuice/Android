package com.example.planer.notifications

import android.R
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMMessageHandler: FirebaseMessagingService()
{
    val MESSAGE_NOTIFICATION_ID = 11

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val from = remoteMessage.from
        val notification: RemoteMessage.Notification? = remoteMessage.notification
        createNotification(notification)
    }

    // Creates notification based on title and body received
    private fun createNotification(notification: RemoteMessage.Notification?) {
        val context: Context = baseContext
        val mBuilder: Notification.Builder = Notification.Builder(context)
            .setSmallIcon(R.drawable.ic_menu_today)
            .setContentTitle("Уведомление")
            .setContentText("Текст")
        val mNotificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build())
    }
}