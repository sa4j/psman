/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ユーザ情報のデータクラス
 */
@Entity(tableName = "users")
data class User @JvmOverloads constructor(
    val serviceName: String,
    val userName: String,
    val password: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var trash: Int = 0,
    var updatedAt: Long = System.currentTimeMillis()
)