/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.sakibeko.psman.user.ListViewModel
import com.sakibeko.psman.user.data.User
import com.sakibeko.psman.user.data.UserRepository
import com.sakibeko.psman.util.view.ViewEvent
import kotlinx.coroutines.launch

/**
 * ViewModel:ユーザ一覧画面
 */
class UserListViewModel(private val mUserRepository: UserRepository) : ListViewModel() {

    /** 一覧に表示するユーザ情報 */
    val mUsers: LiveData<List<User>> = mUserRepository.all()

    /** ユーザの有無 */
    val mIsEmpty: LiveData<Boolean> = Transformations.map(mUsers) {
        it.isEmpty()
    }

    /** イベント:ユーザ照会 */
    val mEventRefer = MutableLiveData<ViewEvent<Int>>()

    /**
     * ユーザ情報の照会
     */
    fun referUser(id: Int) {
        mEventRefer.value = ViewEvent(id)
    }

    /**
     * ユーザ情報の移動
     */
    override fun changeUser(user: User) {
        user.trash = 1
        user.updatedAt = System.currentTimeMillis()
        viewModelScope.launch {
            mUserRepository.save(user)
        }
    }

}