/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.data.source

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakibeko.psman.user.data.User

/**
 * ユーザ情報DAO
 */
@Dao
interface UserDao {

    /** ユーザ情報の全取得 */
    @Query("SELECT * FROM users WHERE trash = 0 ORDER BY serviceName, userName")
    fun all(): LiveData<List<User>>

    /** ごみ箱の全取得 */
    @Query("SELECT * FROM users WHERE trash <> 0 ORDER BY updatedAt DESC")
    fun trashAll(): LiveData<List<User>>

    /** ユーザ情報の取得 */
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUser(id: Int): User

    /** ユーザ情報の保存 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: User)

    /** ゴミ箱の削除 */
    @Query("DELETE FROM users WHERE trash <> 0")
    suspend fun emptyTrash()

}