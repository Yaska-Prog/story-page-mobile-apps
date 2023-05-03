package com.bangkit.mystoryapps.UI.Login

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bangkit.mystoryapps.Utils.EspressoIdlingResource
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.Intents.intended
import com.bangkit.mystoryapps.UI.Main.MainActivity
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.bangkit.mystoryapps.UI.Landing.LandingActivity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest{
    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login_success(){
        Intents.init()
        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.txtUsernameLogin)).perform(
            ViewActions.click(),
            ViewActions.typeText("Yaska@gmail.com"))
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.txtPassword)).perform(
            ViewActions.click(),
            ViewActions.typeText("Yaska321"), ViewActions.closeSoftKeyboard())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.btnLogin)).perform(
            ViewActions.click())
        Thread.sleep(10000)
        intended(hasComponent(MainActivity::class.java.name))
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.rvStory)).check(matches(
            ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(com.bangkit.mystoryapps.R.id.btnLogout)).perform(
            ViewActions.click())
        intended(hasComponent(LandingActivity::class.java.name))
    }
}