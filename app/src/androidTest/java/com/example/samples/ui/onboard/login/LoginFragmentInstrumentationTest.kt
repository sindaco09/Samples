package com.example.samples.ui.onboard.login

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.samples.R
import org.junit.Test
import org.junit.runner.RunWith

/*
 * For more information on types of fragment testing:
 * https://developer.android.com/guide/fragments/test
 */
@RunWith(AndroidJUnit4::class)
class LoginFragmentInstrumentationTest {

    @Test fun testLoginFragment() {
        val scenario = launchFragmentInContainer<LoginFragment>()


    }
}