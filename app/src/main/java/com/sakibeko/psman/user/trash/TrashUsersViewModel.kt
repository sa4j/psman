/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.trash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.sakibeko.psman.R
import com.sakibeko.psman.user.ListViewModel
import com.sakibeko.psman.user.data.User
import com.sakibeko.psman.user.data.UserRepository
import com.sakibeko.psman.util.view.ViewEvent
import kotlinx.coroutines.launch

/**
 * ViewModel:ユーザ一覧画面（ゴミ箱）
 */
class TrashUsersViewModel(private val mUserRepository: UserRepository) : ListViewModel() {

    /** 一覧に表示するユーザ情報 */
    val mUsers: LiveData<List<User>> = mUserRepository.allTrash()

    /** ユーザの有無 */
    val mIsEmpty: LiveData<Boolean> = Transformations.map(mUsers) {
        it.isEmpty()
    }

    /** イベント:削除成功 */
    val mEventOk = MutableLiveData<ViewEvent<Int>>()

    /**
     * ゴミ箱の削除
     */
    fun emptyTrash() {
        viewModelScope.launch {
            mUserRepository.emptyTrash()
        }
        mEventOk.value = ViewEvent(R.string.info_msg_clear_trash)
    }

    /**
     * ユーザ情報の移動
     */
    override fun changeUser(user: User) {
        user.trash = 0
        user.updatedAt = System.currentTimeMillis()
        viewModelScope.launch {
            mUserRepository.save(user)
        }
    }

}