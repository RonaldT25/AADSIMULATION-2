package com.dicoding.habitapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.ui.detail.DetailHabitActivity
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val habitId = inputData.getInt(HABIT_ID, 0)
    private val habitTitle = inputData.getString(HABIT_TITLE)

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(applicationContext, DetailHabitActivity::class.java).apply {
            putExtra(HABIT_ID, habitId)
        }

        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

    }
    override fun doWork(): Result {
        val prefManager = androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify = prefManager.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)
        //TODO 12 : If notification preference on, show notification with pending intent
        if (shouldNotify == true){
            val notification = habitTitle?.let {
                NotificationCompat.Builder(
                    applicationContext,
                    it
                )
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentTitle(habitTitle)
                    .setContentText(applicationContext.resources.getString(R.string.notify_content))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setContentIntent(getPendingIntent())
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val channelName2 = "Channel Name"
                val channelDescription = "Channel Description"
                val channelImportance = NotificationManager.IMPORTANCE_HIGH

                val channel = NotificationChannel(habitTitle, channelName2, channelImportance).apply {
                    description = channelDescription
                }

                val notificationManager = applicationContext.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

                notificationManager.createNotificationChannel(channel)
            }


            with(NotificationManagerCompat.from(applicationContext)) {
                if (notification != null) {
                    notify(0, notification.build())
                }
            }
        }
        return Result.success()
    }

}
