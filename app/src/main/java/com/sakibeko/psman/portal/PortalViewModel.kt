/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.portal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sakibeko.psman.R
import com.sakibeko.psman.auth.Auth
import com.sakibeko.psman.util.view.ViewEvent

/**
 * ViewModel:ログイン画面
 */
class PortalViewModel(private val mAuth: Auth) : ViewModel() {

    /** サインイン:パスワード */
    val mSignKey1 = MutableLiveData<String>()

    /** サインイン:パスワード（確認用） */
    val mSignKey2 = MutableLiveData<String>()

    /**
     * サインイン:状態
     * true: サインイン済み  false: それ以外
     */
    val mHasSign = MutableLiveData(mAuth.hasSign())

    /** ログイン:パスワード */
    val mLoginKey = MutableLiveData<String>()

    /** イベント:ログイン成功 */
    val mEventLogin = MutableLiveData<ViewEvent<Unit>>()

    /** イベント:メッセージ通知 */
    val mEventMessage = MutableLiveData<ViewEvent<Int>>()


    /**
     * サインインを試行する
     */
    fun sign() {
        val password1 = mSignKey1.value ?: ""
        val password2 = mSignKey2.value ?: ""

        // Null判定
        if (password1 == "" || password2 == "") {
            mEventMessage.value = ViewEvent(R.string.error_msg_input_has_null)
            return
        }

        // 長さ判定
        if (password1.length < 8) {
            mEventMessage.value = ViewEvent(R.string.error_msg_password_short)
            return
        }

        // 一致判定
        if (password1 != password2) {
            mEventMessage.value = ViewEvent(R.string.error_msg_password_mismatch)
            return
        }

        mAuth.sign(password1)
        mHasSign.value = true
        mEventMessage.value = ViewEvent(R.string.info_msg_signed)
    }

    /**
     * ログインを試行する
     */
    fun login() {
        val password = mLoginKey.value ?: ""

        // Null判定
        if (password == "") {
            mEventMessage.value = ViewEvent(R.string.error_msg_input_has_null)
            return
        }

        if (mAuth.login(password)) {
            mEventLogin.value = ViewEvent(Unit)
        } else {
            mEventMessage.value = ViewEvent(R.string.error_msg_password_wrong)
        }
    }

}