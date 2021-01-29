/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.sakibeko.psman.portal.PortalActivity
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * 単体試験 for PortalActivity
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestPortalActivity {

    companion object {
        const val KEY_SHORT = "1234567"
        const val KEY_CORRECT = KEY_SHORT + "8"
    }

    private lateinit var mActivityScenario: ActivityScenario<PortalActivity>

    @Before
    fun setUp() {
        mActivityScenario = ActivityScenario.launch(PortalActivity::class.java)
    }

    @After
    fun tearDown() {
        mActivityScenario.close()
    }

    @Test
    fun test0010SignNoInput() {
        examineSign(key1 = "", key2 = "")
    }

    @Test
    fun test0020SignKey1Empty() {
        examineSign(key1 = "")
    }

    @Test
    fun test0030SignKey2Empty() {
        examineSign(key2 = "")
    }

    @Test
    fun test0040SignKeyShort() {
        examineSign(key1 = KEY_SHORT, key2 = KEY_SHORT)
    }

    @Test
    fun test0050SignKeyMismatched() {
        examineSign(key1 = KEY_CORRECT + "a", key2 = KEY_CORRECT + "b")
    }

    @Test
    fun test0060SignKeyMatched() {
        examineSign()
    }

    @Test
    fun test0070LoginKeyEmpty() {
        examineLogin(key = "")
    }

    @Test
    fun test0080LoginKeyMismatched() {
        examineLogin(KEY_SHORT)
    }

    @Test
    fun test0090LoginKeyMatched() {
        examineLogin(KEY_CORRECT)
    }

    /**
     * サイン画面を試験する
     */
    private fun examineSign(key1: String = KEY_CORRECT, key2: String = KEY_CORRECT) {
        onView(withId(R.id.edit_sign_key1)).perform(typeText(key1))
        onView(withId(R.id.edit_sign_key2)).perform(typeText(key2))
        onView(withId(R.id.fab_login)).perform(click())
    }

    /**
     * ログイン画面を試験する
     */
    private fun examineLogin(key: String) {
        onView(withId(R.id.edit_login_key)).perform(typeText(key))
        onView(withId(R.id.fab_login)).perform(click())
    }

}