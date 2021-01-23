/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sakibeko.psman.R
import com.sakibeko.psman.databinding.FragmentUserListBinding
import com.sakibeko.psman.databinding.FragmentUserListItemBinding
import com.sakibeko.psman.user.UserListAdapter
import com.sakibeko.psman.user.data.User
import com.sakibeko.psman.util.ARG_NAME_RESULT
import com.sakibeko.psman.util.RESULT_SAVE_OK
import com.sakibeko.psman.util.getViewModelFactory
import com.sakibeko.psman.util.view.EventObserver

/**
 * Fragment:ユーザ一覧画面
 */
class UserListFragment : Fragment() {

    /** ViewModel用オブジェクト */
    private val mViewModel by viewModels<UserListViewModel> { getViewModelFactory() }

    /** データバインディング用オブジェクト */
    private lateinit var mViewDataBinding: FragmentUserListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // データバインディングの各種設定
        mViewDataBinding = FragmentUserListBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return mViewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViewEvents()
        setupListAdapter()
        setupFab()
    }

    /**
     * 画面関連イベントを初期化する
     */
    private fun setupViewEvents() {
        // ユーザ情報の保存に成功した際のイベント
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(
            ARG_NAME_RESULT
        )?.observe(viewLifecycleOwner) { resId ->
            when (resId) {
                RESULT_SAVE_OK -> Snackbar.make(
                    requireView(),
                    R.string.info_msg_save_successful,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Int>(ARG_NAME_RESULT)
        }
        // ユーザ一覧からユーザを選択した際のイベント
        mViewModel.mEventRefer.observe(viewLifecycleOwner, EventObserver { userId ->
            if (userId == 0) {
                return@EventObserver
            }
            val action = UserListFragmentDirections.actionListToDetail(userId)
            findNavController().navigate(action)
        })
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
            mViewDataBinding.userList.adapter as UserListAdapter
        )
        helper.attachToRecyclerView(mViewDataBinding.root.findViewById(R.id.user_list))
    }

    /**
     * FABの初期化
     */
    private fun setupFab() {
        requireActivity().findViewById<View>(R.id.fab_add_user).setOnClickListener {
            val action = UserListFragmentDirections.actionListToDetail()
            findNavController().navigate(action)
        }
    }

    /**
     * ListAdapter
     */
    private class UserListAdapterExt(private val viewModel: UserListViewModel) :
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
    private class ViewHolder private constructor(private val binding: FragmentUserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            /**
             * ViewHolderの変換（データバインディングの設定）
             */
            fun convert(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentUserListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        /**
         * データバインディングの各種設定
         */
        fun bind(viewModel: UserListViewModel, item: User) {
            binding.viewModel = viewModel
            binding.user = item
            binding.executePendingBindings()
        }
    }

}