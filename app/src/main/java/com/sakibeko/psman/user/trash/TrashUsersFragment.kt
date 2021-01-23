/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.trash

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sakibeko.psman.R
import com.sakibeko.psman.databinding.FragmentUserTrashBinding
import com.sakibeko.psman.databinding.FragmentUserTrashItemBinding
import com.sakibeko.psman.user.UserListAdapter
import com.sakibeko.psman.user.data.User
import com.sakibeko.psman.util.getViewModelFactory
import com.sakibeko.psman.util.view.EventObserver

/**
 * Fragment:ユーザ一覧画面（ゴミ箱）
 */
class TrashUsersFragment : Fragment() {

    /** ViewModel用オブジェクト */
    private val mViewModel by viewModels<TrashUsersViewModel> { getViewModelFactory() }

    /** データバインディング用オブジェクト */
    private lateinit var mViewDataBinding: FragmentUserTrashBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // データバインディングの各種設定
        mViewDataBinding = FragmentUserTrashBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return mViewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupListAdapter()
        setupViewEvents()
    }

    /**
     * ListAdapterの初期化
     */
    private fun setupListAdapter() {
        val viewModel = mViewDataBinding.viewModel
            ?: throw IllegalStateException("Failed to initialize ViewModel.")

        mViewDataBinding.userList.adapter = UserListAdapterExt(viewModel)

        val helper = UserListAdapter.SwipeToDismissTouchHelper(
            requireContext(),
            mViewDataBinding.userList.adapter as UserListAdapter,
            R.drawable.ic_restore,
            Color.GREEN
        )
        helper.attachToRecyclerView(mViewDataBinding.root.findViewById(R.id.user_list))
    }

    /**
     * 画面関連イベントを初期化する
     */
    private fun setupViewEvents() {
        // ゴミ箱の削除に成功した際のイベント
        mViewModel.mEventOk.observe(viewLifecycleOwner, EventObserver {
            // メッセージを表示する
            Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
        })
    }

    /**
     * ListAdapter
     */
    private class UserListAdapterExt(private val viewModel: TrashUsersViewModel) :
        UserListAdapter<ViewHolder>(viewModel) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder.convert(parent)

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            holder.bind(viewModel, item)
        }
    }

    /**
     * リストの各要素のViewを保持するクラス
     */
    private class ViewHolder private constructor(private val binding: FragmentUserTrashItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            /**
             * ViewHolderの変換（データバインディングの設定）
             */
            fun convert(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentUserTrashItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        /**
         * データバインディングの各種設定
         */
        fun bind(viewModel: TrashUsersViewModel, item: User) {
            binding.viewModel = viewModel
            binding.user = item
            binding.executePendingBindings()
        }
    }

}