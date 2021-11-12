package com.dicoding.habitapp.data

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//TODO 1 : Define a local database table using the schema in app/schema/habits.json
@Parcelize
@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @NonNull val title: String,
    @NonNull val minutesFocus: Long,
    @NonNull val startTime: String,
    @NonNull val priorityLevel: String
): Parcelable
