/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.list

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sakibeko.psman.user.UserListAdapter
import com.sakibeko.psman.user.data.User

/**
 * app:items要素の宣言
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<User>?) {
    items?.let {
        (listView.adapter as UserListAdapter).submitList(items)
    }
}
