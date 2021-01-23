/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.util

import android.content.Context
import androidx.room.Room
import com.sakibeko.psman.user.data.UserRepository
import com.sakibeko.psman.user.data.source.DefaultUserRepository
import com.sakibeko.psman.user.data.source.UserDatabase

/**
 * ServiceLocatorクラス
 */
object ServiceLocator {

    /**
     * ユーザ情報の管理API
     */
    private var mUserRepository: UserRepository? = null


    /**
     * ユーザ情報の管理APIの取得
     */
    fun provideUserRepository(context: Context): UserRepository {
        synchronized(this) {
            return mUserRepository ?: createUserRepository(context)
        }
    }

    /**
     * ユーザ情報の管理APIの生成
     */
    private fun createUserRepository(context: Context): UserRepository {
        val newRepo = createLocalUserRepository(context)
        mUserRepository = newRepo
        return newRepo
    }

    /**
     * ユーザ情報の管理APIの生成(ローカルストレージ)
     */
    private fun createLocalUserRepository(context: Context): UserRepository {
        val database = createDataBase(context)
        return DefaultUserRepository(database.userDao())
    }

    /**
     * ユーザ情報の管理DBの生成
     */
    private fun createDataBase(context: Context): UserDatabase {
        return Room.databaseBuilder(
            context.applicationContext, UserDatabase::class.java, UserDatabase.NAME
        ).build()
    }

}