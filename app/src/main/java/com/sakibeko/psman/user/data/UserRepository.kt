/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.data

import androidx.lifecycle.LiveData

/**
 * ユーザ情報の管理API
 */
interface UserRepository {

    /** ユーザ情報の全取得 */
    fun all(): LiveData<List<User>>

    /** ごみ箱の全取得 */
    fun allTrash(): LiveData<List<User>>

    /** ユーザ情報の取得 */
    suspend fun getUser(id: Int): User

    /** ユーザ情報の保存 */
    suspend fun save(user: User)

    /** ゴミ箱の削除 */
    suspend fun emptyTrash()

}