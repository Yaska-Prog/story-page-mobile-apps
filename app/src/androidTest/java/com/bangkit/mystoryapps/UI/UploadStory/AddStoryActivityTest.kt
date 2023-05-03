package com.bangkit.mystoryapps.UI.UploadStory

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bangkit.mystoryapps.UI.Main.MainActivity
import com.bangkit.mystoryapps.Utils.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddStoryActivityTest{
    @get:Rule
    val activity = ActivityScenarioRule(AddStoryActivity::class.java)

    @Before
    fun setUp(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun addStory_success(){
        Intents.init()
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.switchLocation)).perform(
            ViewActions.click())
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.txtDescriptionAddStory)).perform(
            ViewActions.click(),
            ViewActions.typeText("Ini adalah testing"))
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.btnUpload)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.btnStartCameraX)).perform(
            ViewActions.click())

        Intents.intended(IntentMatchers.hasComponent(CameraActivity::class.java.name))
        Intents.release()
        Intents.init()
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.captureImage)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.captureImage)).perform(
            ViewActions.click())
        Thread.sleep(2000)
        Intents.release()
        Intents.init()

        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.btnUpload)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.btnUpload)).perform(
            ViewActions.click())
        Thread.sleep(2000)
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.rvStory)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }
}