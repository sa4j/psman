/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.sakibeko.psman.databinding.FragmentUserDetailBinding
import com.sakibeko.psman.user.UserEditActivity
import com.sakibeko.psman.util.ARG_NAME_RESULT
import com.sakibeko.psman.util.RESULT_SAVE_OK
import com.sakibeko.psman.util.getViewModelFactory
import com.sakibeko.psman.util.view.EventObserver

/**
 * Fragment:ユーザ詳細
 */
class UserDetailFragment : Fragment() {

    /** ViewModel用オブジェクト */
    private val mViewModel by viewModels<UserDetailViewModel> { getViewModelFactory() }

    /** データバインディング用オブジェクト */
    private lateinit var mViewDataBinding: FragmentUserDetailBinding

    /** 画面遷移パラメータ */
    private val mArgs: UserDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = FragmentUserDetailBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewEvents()
        mViewModel.start(mArgs.userId)
    }

    /**
     * 画面関連イベントを初期化する
     */
    private fun setupViewEvents() {
        // ユーザ情報の保存に成功した際のイベント
        mViewModel.mEventOk.observe(viewLifecycleOwner, EventObserver {
            // 前の画面に結果を渡す
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                ARG_NAME_RESULT, RESULT_SAVE_OK
            )
            // 前の画面に戻る
            (requireActivity() as UserEditActivity).onSupportNavigateUp()
        })
        // ユーザ情報の保存に失敗した際のイベント
        mViewModel.mEventError.observe(viewLifecycleOwner, EventObserver {
            // エラーメッセージを表示する
            Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
        })
    }

}