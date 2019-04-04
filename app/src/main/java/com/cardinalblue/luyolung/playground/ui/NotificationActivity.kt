package com.cardinalblue.luyolung.playground.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cardinalblue.luyolung.playground.MainActivity
import com.cardinalblue.luyolung.playground.R
import com.cardinalblue.luyolung.util.subscribeUntil
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.subjects.CompletableSubject
import kotlinx.android.synthetic.main.activity_notification.*
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.cardinalblue.luyolung.util.observableTimer
import io.reactivex.schedulers.Schedulers

class NotificationActivity: AppCompatActivity() {

    private val lifeCycle = CompletableSubject.create()

    private var notificationManager: NotificationManager? = null
    private val channelID = "com.cardinalblue.luyolung.playground"
    private val channel2ID = "com.cardinalblue.luyolung.playground2"

    var notificationId = 1

    val resultIntent = Intent(this, MainActivity::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        notificationManager =
            getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(
            channelID,
            "NotifyDemo News",
            "Example News Channel")

        createProgressNotificationChannel(
            channel2ID,
            "Notify Progress",
            "Download Progress Channel")

        RxView.clicks(function_x_btn)
            .subscribeUntil(lifeCycle) {
                sendSimpleNotification()
            }

        RxView.clicks(function_progress_btn)
            .subscribeUntil(lifeCycle) {
                sendProgressNotification()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifeCycle.onComplete()
    }

    private fun sendSimpleNotification() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

            val notification = Notification.Builder(
                this@NotificationActivity,
                channelID)
                .setContentTitle("Example Notification")
                .setContentText("This is an notification: $notificationId")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setChannelId(channelID)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager?.notify(notificationId, notification)

            notificationId ++
        }
    }

    private fun sendProgressNotification() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val builder = NotificationCompat.Builder(this, channel2ID).apply {
                setContentTitle("Picture Download")
                setContentText("Download in progress")
                setSmallIcon(android.R.drawable.ic_btn_speak_now)
                setPriority(NotificationCompat.PRIORITY_LOW)
            }

            val PROGRESS_MAX = 100
            val PROGRESS_CURRENT = 0
            NotificationManagerCompat.from(this).apply {
                val id = notificationId

                // Issue the initial notification with zero progress
                builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
                notify(id, builder.build())

                observableTimer()
                    // time -> progress
                    .map { it * 10 }
                    .observeOn(Schedulers.computation())
                    .doOnComplete {
                        builder.setContentText("Download complete").setProgress(0, 0, false)
                        notify(id, builder.build())
                    }
                    .subscribeUntil(lifeCycle) {
                        builder.setProgress(PROGRESS_MAX, it, false)
                        notify(id, builder.build())
                    }

                notificationId ++
            }
        }
    }

    private fun createNotificationChannel(id: String, name: String,
                                          description: String) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(id, name, importance)

            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun createProgressNotificationChannel(id: String, name: String,
                                                  description: String) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(id, name, importance)

            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}