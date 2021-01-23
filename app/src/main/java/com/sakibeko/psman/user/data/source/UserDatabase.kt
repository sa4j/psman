/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sakibeko.psman.user.data.User

/**
 * ユーザ情報の管理DB
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    companion object {
        /** DB名 */
        const val NAME = "Users.db"
    }

    /** DAOの取得 */
    abstract fun userDao(): UserDao
}