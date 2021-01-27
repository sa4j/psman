/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakibeko.psman.R
import com.sakibeko.psman.auth.Auth
import com.sakibeko.psman.auth.PasswordEncoder
import com.sakibeko.psman.user.data.User
import com.sakibeko.psman.user.data.UserRepository
import com.sakibeko.psman.util.view.ViewEvent
import kotlinx.coroutines.launch

/**
 * ViewModel:ユーザ詳細
 */
class UserDetailViewModel(private val mUserRepository: UserRepository, private val mAuth: Auth) :
    ViewModel() {

    /** ユーザID */
    private var mUserId: Int = 0

    /** テキスト:サービス名 */
    val mServiceName = MutableLiveData<String>()

    /** テキスト:ユーザ名 */
    val mUserName = MutableLiveData<String>()

    /** テキスト:パスワード */
    val mPassword = MutableLiveData<String>()

    /** イベント:保存成功 */
    val mEventOk = MutableLiveData<ViewEvent<Unit>>()

    /** イベント:エラー通知 */
    val mEventError = MutableLiveData<ViewEvent<Int>>()


    /**
     * ユーザ情報の表示
     */
    fun start(userId: Int) {
        if (userId == 0) {
            return// 新規作成時は何もしない
        }

        viewModelScope.launch {
            mUserRepository.getUser(userId).let { result ->
                val keys = mAuth.getCipherKeys()
                mPassword.value = try {
                    PasswordEncoder.decode(result.password, keys.first, keys.second)
                } catch (e: IllegalStateException) {
                    mEventError.value = ViewEvent(R.string.error_msg_retry_login)
                    return@let
                }
                mUserId = userId
                mServiceName.value = result.serviceName
                mUserName.value = result.userName
            }
        }
    }

    /**
     * ユーザ情報の保存
     */
    fun saveUser() {
        val serviceName = mServiceName.value ?: ""
        val userName = mUserName.value ?: ""
        val password = mPassword.value ?: ""

        // Null判定
        if (serviceName == "" || userName == "" || password == "") {
            mEventError.value = ViewEvent(R.string.error_msg_input_has_null)
            return
        }

        val keys = mAuth.getCipherKeys()
        val encryptedPassword = try {
            PasswordEncoder.encode(password, keys.first, keys.second)
        } catch (e: IllegalStateException) {
            mEventError.value = ViewEvent(R.string.error_msg_retry_login)
            return
        }

        val user = User(serviceName, userName, encryptedPassword, mUserId)
        viewModelScope.launch {
            // ユーザ情報の保存
            mUserRepository.save(user)
            mEventOk.value = ViewEvent(Unit)
        }
    }

}
