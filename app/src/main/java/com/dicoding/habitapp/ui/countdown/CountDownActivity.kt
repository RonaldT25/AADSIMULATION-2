package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIFICATION_CHANNEL_ID
import java.util.concurrent.TimeUnit

class CountDownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        val habit = intent.getParcelableExtra<Habit>(HABIT) as Habit

        findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

        val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)

        //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished
        viewModel.setInitialTime(habit.minutesFocus)
        viewModel.currentTimeString.observe(this, Observer{
            findViewById<TextView>(R.id.tv_count_down).text = it
        })
        viewModel.eventCountDownFinish.observe(this, Observer(this::updateButtonState))

        //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up.
        val title: Data = workDataOf(HABIT_TITLE to habit.title , HABIT_ID to habit.id)

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            viewModel.startTimer()
            val workRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                .setInputData(title)
                .setInitialDelay(habit.minutesFocus,TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(this).enqueueUniqueWork(NOTIFICATION_CHANNEL_ID,
                ExistingWorkPolicy.KEEP,workRequest)
            updateButtonState(false)
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            viewModel.stopTimer()
            viewModel.setInitialTime(habit.minutesFocus)
            WorkManager.getInstance(this).cancelUniqueWork(NOTIFICATION_CHANNEL_ID)
            updateButtonState(true)
        }
    }

    private fun updateButtonState(finished: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = finished
        findViewById<Button>(R.id.btn_stop).isEnabled = !finished
    }
}