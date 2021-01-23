/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.util

import android.app.Application
import com.sakibeko.psman.auth.Auth
import com.sakibeko.psman.auth.AuthImpl
import com.sakibeko.psman.user.data.UserRepository

/**
 * 独自Applicationクラス
 */
class MyApplication : Application() {

    /** ユーザ情報の管理API */
    val mUserRepository: UserRepository
        get() = ServiceLocator.provideUserRepository(this)

    /** ユーザ認証 */
    lateinit var mAuth: Auth


    override fun onCreate() {
        super.onCreate()

        mAuth = AuthImpl(this)
    }
}