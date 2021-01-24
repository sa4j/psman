/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.portal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.sakibeko.psman.databinding.FragmentPortalBinding
import com.sakibeko.psman.user.UserEditActivity
import com.sakibeko.psman.util.getViewModelFactory
import com.sakibeko.psman.util.view.EventObserver

/**
 * Fragment:ログイン画面
 */
class PortalFragment : Fragment() {

    /** ViewModel用オブジェクト */
    private val mViewModel by viewModels<PortalViewModel> { getViewModelFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewDataBinding = FragmentPortalBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewEvents(view)
    }

    /**
     * 画面関連イベントを初期化する
     */
    private fun setupViewEvents(view: View) {
        // ログイン成功時に[ユーザ管理画面]に遷移する
        mViewModel.mEventLogin.observe(viewLifecycleOwner, EventObserver {
            startActivity(Intent(requireContext(), UserEditActivity::class.java))
        })
        // メッセージ通知
        mViewModel.mEventMessage.observe(viewLifecycleOwner, EventObserver {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
        })
    }

}