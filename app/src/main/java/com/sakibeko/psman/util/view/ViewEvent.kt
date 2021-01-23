/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.util.view

import androidx.lifecycle.Observer

/**
 * 画面遷移イベント
 */
open class ViewEvent<out T>(private val content: T) {

    /**
     * 画面の再表示時にイベントが繰り返し表示されることを防止するフラグ
     */
    private var mHasBeenHandled = false

    /**
     * イベントを返す（実行済みの場合、nullを返す）
     */
    fun getContentIfNotHandled(): T? {
        return if (mHasBeenHandled) {
            null
        } else {
            mHasBeenHandled = true// イベント実行済み
            content
        }
    }

}

/**
 * イベント監視コールバック
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<ViewEvent<T>> {
    override fun onChanged(event: ViewEvent<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}