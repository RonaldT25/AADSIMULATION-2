package com.dicoding.habitapp.ui.list

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dicoding.habitapp.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//TODO 16 : Write UI test to validate when user tap Add Habit (+), the AddHabitActivity displayed
class HabitActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(HabitListActivity::class.java)

    @Before
    fun setUp() {
        ActivityScenario.launch(HabitListActivity::class.java)
    }

    @Test
    fun loadAddTask() {
        Espresso.onView(withId(R.id.fab)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.add_habit_activity))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}