/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sakibeko.psman.user.UserEditActivity
import com.sakibeko.psman.util.MyApplication
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 単体試験 for UserEditActivity（画面遷移）
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
class TestUserEditActivityMovingScreen {

    companion object {
        const val SERVICE_NAME_CORRECT = "Google"
        const val USER_NAME_CORRECT = "sakibeko@gmail.com"
        const val PASSWORD_CORRECT = "gglskb1234"
    }

    private lateinit var mActivityScenario: ActivityScenario<UserEditActivity>
    private lateinit var mActivity: UserEditActivity

    @Before
    fun setUp() {
        mActivityScenario = ActivityScenario.launch(UserEditActivity::class.java)
        mActivityScenario.onActivity { activity ->
            mActivity = activity
        }
    }

    @After
    fun tearDown() {
        mActivityScenario.close()
    }

    /**
     * 画面遷移: 一覧 -> 詳細 -> 一覧（バックキーで戻る）
     */
    @Test
    fun moveToDetailAndBackByPressBack() {
        // 最初の画面: [一覧]
        onView(withText(R.string.fragment_label_user_list)).check(matches(isDisplayed()))
        // [一覧] -> [詳細]
        moveToDetail()
        onView(withText(R.string.fragment_label_user_edit)).check(matches(isDisplayed()))
        // [詳細] -> [一覧]
        pressBack()
        onView(withText(R.string.fragment_label_user_list)).check(matches(isDisplayed()))
    }

    /**
     * 画面遷移: 一覧 -> 詳細 -> 一覧（ツールバーのupで戻る）
     */
    @Test
    fun moveToDetailAndBackByToolbar() {
        // 最初の画面: [一覧]
        onView(withText(R.string.fragment_label_user_list)).check(matches(isDisplayed()))
        // [一覧] -> [詳細]
        moveToDetail()
        onView(withText(R.string.fragment_label_user_edit)).check(matches(isDisplayed()))
        // [詳細] -> [一覧]
        backByToolbar()
        onView(withText(R.string.fragment_label_user_list)).check(matches(isDisplayed()))
    }

    /**
     * 画面遷移: 一覧 -> 詳細 -> 一覧（意図的にパスワードの保存に成功させる）
     */
    @Test
    fun moveToDetailAndSaveCorrectly() {
        loginIfNeeded()

        // 最初の画面: [一覧]
        onView(withText(R.string.fragment_label_user_list)).check(matches(isDisplayed()))
        // [一覧] -> [詳細]
        moveToDetail()
        onView(withText(R.string.fragment_label_user_edit)).check(matches(isDisplayed()))
        // [詳細] -> [一覧]
        attemptSaveUser()// パスワードの保存(成功させる)
        onView(withText(R.string.fragment_label_user_list)).check(matches(isDisplayed()))
    }

    /**
     * 画面遷移: 一覧 -> 詳細 -> 詳細（意図的にパスワードの保存に失敗させる）
     */
    @Test
    fun moveToDetailAndSaveIncorrectly() {
        // 最初の画面: [一覧]
        onView(withText(R.string.fragment_label_user_list)).check(matches(isDisplayed()))
        // [一覧] -> [詳細]
        moveToDetail()
        onView(withText(R.string.fragment_label_user_edit)).check(matches(isDisplayed()))
        // [詳細] -> [一覧]
        attemptSaveUser(serviceName = "")// パスワードの保存(失敗させる)
        onView(withText(R.string.fragment_label_user_edit)).check(matches(isDisplayed()))
    }

    /**
     * 画面遷移: 一覧 -> ゴミ箱 -> 一覧（バックキーで戻る）
     */
    @Test
    fun moveToTrashAndBackByPressBack() {
        // 最初の画面: [一覧]
        onView(withText(R.string.fragment_label_user_list)).check(matches(isDisplayed()))
        // [一覧] -> [ゴミ箱]
        moveToTrash()
        onView(
            allOf(
                withText(R.string.fragment_label_user_trash),
                withParent(withId(R.id.toolbar))
            )
        ).check(matches(isDisplayed()))
        // [ゴミ箱] -> [一覧]
        pressBack()
        onView(withText(R.string.fragment_label_user_list)).check(matches(isDisplayed()))
    }

    /**
     * 画面遷移: 一覧 -> ゴミ箱 -> 一覧（ツールバーのupで戻る）
     */
    @Test
    fun moveToTrashAndBackByToolbar() {
        // 最初の画面: [一覧]
        onView(withText(R.string.fragment_label_user_list)).check(matches(isDisplayed()))
        // [一覧] -> [ゴミ箱]
        moveToTrash()
        onView(
            allOf(
                withText(R.string.fragment_label_user_trash),
                withParent(withId(R.id.toolbar))
            )
        ).check(matches(isDisplayed()))
        // [ゴミ箱] -> [一覧]
        backByToolbar()
        onView(withText(R.string.fragment_label_user_list)).check(matches(isDisplayed()))
    }

    /**
     * 詳細画面へ遷移する
     */
    private fun moveToDetail() = onView(withId(R.id.fab_add_user)).perform(click())

    /**
     * ゴミ画面へ遷移する
     */
    private fun moveToTrash() {
        onView(withContentDescription(mActivity.getString(R.string.nav_app_bar_open_drawer_description)))
            .perform(click())
        onView(withId(R.id.dest_trash_list)).perform(click())
    }

    /**
     * ツールバーのupを用いて、前の画面に遷移する
     */
    private fun backByToolbar() =
        onView(withContentDescription(mActivity.getString(R.string.nav_app_bar_navigate_up_description)))
            .perform(click())

    private fun loginIfNeeded() {
        val auth = (mActivity.application as MyApplication).mAuth
        val password = "password"

        if (!auth.hasLogin()) {
            auth.sign(password)
        }
        if (!auth.hasLogin()) {
            auth.login(password)
        }
    }

    /**
     * パスワードの保存を試行する（一覧画面に遷移する）
     */
    private fun attemptSaveUser(
        serviceName: String = TestUserListFragment.SERVICE_NAME_CORRECT,
        userName: String = TestUserListFragment.USER_NAME_CORRECT,
        password: String = TestUserListFragment.PASSWORD_CORRECT
    ) {
        onView(withId(R.id.edit_service_name)).perform(ViewActions.typeText(serviceName))
        onView(withId(R.id.edit_user_name)).perform(ViewActions.typeText(userName))
        onView(withId(R.id.edit_password)).perform(ViewActions.typeText(password))
        onView(withId(R.id.fab_save_user)).perform(click())
    }

}