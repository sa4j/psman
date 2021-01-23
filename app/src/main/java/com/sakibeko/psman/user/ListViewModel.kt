/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user

import androidx.lifecycle.ViewModel
import com.sakibeko.psman.user.data.User
import com.sakibeko.psman.util.FORMAT_UPDATED_AT
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

abstract class ListViewModel : ViewModel() {

    /** updatedAtのフォーマット変換 */
    private val mDateFormat: DateFormat = SimpleDateFormat(FORMAT_UPDATED_AT, Locale.getDefault())

    /**
     * updatedAtを表示用に変換する
     */
    fun displayUpdatedAt(updatedAt: Long): String = mDateFormat.format(Date(updatedAt))

    /**
     * ユーザ情報の変更
     */
    abstract fun changeUser(user: User)

}