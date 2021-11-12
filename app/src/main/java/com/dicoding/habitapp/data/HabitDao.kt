package com.dicoding.habitapp.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

//TODO 2 : Define data access object (DAO)
@Dao
interface HabitDao {
    @RawQuery(observedEntities = [Habit::class])
    fun getHabits(query: SupportSQLiteQuery): DataSource.Factory<Int, Habit>
    @Query("SELECT * FROM Habit WHERE id IN (:habitId)")
    fun getHabitById(habitId: Int): LiveData<Habit>
    @Insert
    fun insertHabit(habit: Habit)
    @Insert
    fun insertAll(vararg habits: Habit)
    @Delete
    fun deleteHabit(habits: Habit)
    @Query("SELECT * FROM Habit WHERE priorityLevel IN (:level) LIMIT 1")
    fun getRandomHabitByPriorityLevel(level: String): LiveData<Habit>
}
