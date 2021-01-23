/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakibeko.psman.R
import com.sakibeko.psman.auth.Auth
import com.sakibeko.psman.user.data.User
import com.sakibeko.psman.user.data.UserRepository
import com.sakibeko.psman.util.view.ViewEvent
import kotlinx.coroutines.launch
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

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
                mPassword.value = try {
                    decrypt(result.password)
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

        val encryptedPassword = try {
            encrypt(password)
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

    /**
     * パスワードの暗号化
     */
    private fun encrypt(password: String): String {
        val keys = mAuth.getCipherKeys()
        val cipherKey = SecretKeySpec(keys.first, CIPHER_ALGORITHM)
        val initVector = IvParameterSpec(keys.second)
        val cipher = Cipher.getInstance("$CIPHER_ALGORITHM/$CIPHER_MODE/$CIPHER_PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, cipherKey, initVector)
        val result = cipher.doFinal(password.toByteArray())
        return String(Base64.getEncoder().encode(result))
    }

    /**
     * パスワードの復号化
     */
    private fun decrypt(password: String): String {
        val keys = mAuth.getCipherKeys()
        val cipherKey = SecretKeySpec(keys.first, CIPHER_ALGORITHM)
        val initVector = IvParameterSpec(keys.second)
        val cipher = Cipher.getInstance("$CIPHER_ALGORITHM/$CIPHER_MODE/$CIPHER_PADDING")
        cipher.init(Cipher.DECRYPT_MODE, cipherKey, initVector)
        val result = Base64.getDecoder().decode(password.toByteArray())
        return String(cipher.doFinal(result))
    }

}

/** 暗号化アルゴリズム */
private const val CIPHER_ALGORITHM = "AES"

/** 暗号化モード */
private const val CIPHER_MODE = "CBC"

/** 暗号化パディング方式 */
private const val CIPHER_PADDING = "PKCS5Padding"
