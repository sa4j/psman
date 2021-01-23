/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sakibeko.psman.R
import com.sakibeko.psman.user.data.User

/**
 * RecyclerViewer.Adapter:一覧画面
 */
abstract class UserListAdapter<VH : RecyclerView.ViewHolder>(private val mViewModel: ListViewModel) :
    ListAdapter<User, VH>(UserDiffCallback()) {

    /** 要素（ユーザ情報）を比較する */
    private class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(old: User, new: User): Boolean = old.id == new.id
        override fun areContentsTheSame(old: User, new: User): Boolean = old == new
    }

    /**
     * スワイプ操作でユーザを削除するリスナー
     */
    class SwipeToDismissTouchHelper<VH : RecyclerView.ViewHolder>(
        context: Context,
        adapter: UserListAdapter<VH>,
        iconResId: Int = R.drawable.ic_delete,
        color: Int = Color.RED
    ) : ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ACTION_STATE_IDLE, LEFT) {
        /**
         * ドラッグ操作による並び替えの有無
         */
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false // false: 並び替えしない

        /**
         * スワイプ操作による処理
         */
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val user = adapter.getItem(viewHolder.adapterPosition)
            // スワイプされたユーザ情報を変更する
            adapter.mViewModel.changeUser(user)
        }

        /**
         * 要素の描画処理
         */
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            val itemView = viewHolder.itemView

            // スワイプした要素の右側の背景を赤色で表示する
            val background = ColorDrawable(color)
            background.setBounds(
                itemView.right, itemView.top, itemView.right + dX.toInt(), itemView.bottom
            )
            background.draw(c)

            val deleteIcon = ContextCompat.getDrawable(context, iconResId)
                ?: throw IllegalStateException("Failed to get icon of delete")
            val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

            // 削除アイコンを表示する
            deleteIcon.setBounds(
                itemView.right - deleteIcon.intrinsicWidth - iconMargin,
                itemView.top + iconMargin,
                itemView.right - deleteIcon.intrinsicWidth,
                itemView.bottom - iconMargin
            )
            deleteIcon.draw(c)// （背景に隠れてしまうため）削除アイコンは背景より後に描画する必要がある
        }
    })
}

