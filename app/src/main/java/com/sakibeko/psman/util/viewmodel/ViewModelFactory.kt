/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.util.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.sakibeko.psman.portal.PortalViewModel
import com.sakibeko.psman.user.detail.UserDetailViewModel
import com.sakibeko.psman.user.list.UserListViewModel
import com.sakibeko.psman.user.trash.TrashUsersViewModel
import com.sakibeko.psman.util.MyApplication

/**
 * ViewModelのFactoryクラス
 */
class ViewModelFactory constructor(
    private val application: MyApplication,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String, modelClass: Class<T>, handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(UserListViewModel::class.java) ->
                UserListViewModel(application.mUserRepository)
            isAssignableFrom(UserDetailViewModel::class.java) ->
                UserDetailViewModel(application.mUserRepository, application.mAuth)
            isAssignableFrom(TrashUsersViewModel::class.java) ->
                TrashUsersViewModel(application.mUserRepository)
            isAssignableFrom(PortalViewModel::class.java) ->
                PortalViewModel(application.mAuth)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T

}
