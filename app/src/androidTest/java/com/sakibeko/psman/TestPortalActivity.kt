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
import androidx.test.filters.MediumTest
import com.sakibeko.psman.portal.PortalActivity
import com.sakibeko.psman.portal.PortalFragment
import com.sakibeko.psman.portal.PortalViewModel
import com.sakibeko.psman.util.getInstanceField
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * 単体試験 for PortalActivity
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestPortalActivity {

    companion object {
        const val KEY_SHORT = "1234567"
        const val KEY_CORRECT = KEY_SHORT + "8"
    }

    private lateinit var mActivityScenario: ActivityScenario<PortalActivity>
    private lateinit var mFragment: PortalFragment

    @Before
    fun setUp() {
        mActivityScenario = ActivityScenario.launch(PortalActivity::class.java)
        mActivityScenario.onActivity {
            mFragment = it.supportFragmentManager.fragments[0] as PortalFragment
        }
    }

    @After
    fun tearDown() {
        mActivityScenario.close()
    }

    @Test
    fun test0010SignNoInput() {
        examineSign(key1 = "", key2 = "", messageId = R.string.error_msg_input_has_null)
    }

    @Test
    fun test0020SignKey1Empty() {
        examineSign(key1 = "", messageId = R.string.error_msg_input_has_null)
    }

    @Test
    fun test0030SignKey2Empty() {
        examineSign(key2 = "", messageId = R.string.error_msg_input_has_null)
    }

    @Test
    fun test0040SignKeyShort() {
        examineSign(
            key1 = KEY_SHORT, key2 = KEY_SHORT, messageId = R.string.error_msg_password_short, 8
        )
    }

    @Test
    fun test0050SignKeyMismatched() {
        examineSign(
            key1 = KEY_CORRECT + "a",
            key2 = KEY_CORRECT + "b",
            messageId = R.string.error_msg_password_mismatch
        )
    }

    @Test
    fun test0060SignKeyMatched() {
        examineSign(messageId = R.string.info_msg_signed)
    }

    @Test
    fun test0070LoginKeyEmpty() {
        examineLogin(key = "", messageId = R.string.error_msg_input_has_null)
    }

    @Test
    fun test0080LoginKeyMismatched() {
        examineLogin(KEY_SHORT, messageId = R.string.error_msg_password_wrong)
    }

    @Test
    fun test0090LoginKeyMatched() {
        examineLogin(KEY_CORRECT)
    }

    /**
     * サイン画面を試験する
     */
    private fun examineSign(
        key1: String = KEY_CORRECT,
        key2: String = KEY_CORRECT,
        messageId: Int,
        vararg formatArgs: Any
    ) {
        onView(withId(R.id.edit_sign_key1)).perform(typeText(key1))
        onView(withId(R.id.edit_sign_key2)).perform(typeText(key2))
        onView(withId(R.id.fab_login)).perform(click())

        val viewModel = getInstanceField(mFragment, "mViewModel") as PortalViewModel
        val content = getInstanceField(viewModel.mEventMessage.value!!, "content") as String
        val message = if (formatArgs.isEmpty()) {
            mFragment.getString(messageId)
        } else {
            mFragment.getString(messageId, *formatArgs)
        }
        assertEquals("errorMessage is not expected.", message, content)
    }

    /**
     * ログイン画面を試験する
     */
    private fun examineLogin(key: String, messageId: Int = 0) {
        onView(withId(R.id.edit_login_key)).perform(typeText(key))
        onView(withId(R.id.fab_login)).perform(click())

        if (messageId != 0) {
            val viewModel = getInstanceField(mFragment, "mViewModel") as PortalViewModel
            val content = getInstanceField(viewModel.mEventMessage.value!!, "content") as String
            val message = mFragment.getString(messageId)
            assertEquals("errorMessage is not expected.", message, content)
        }
    }

}