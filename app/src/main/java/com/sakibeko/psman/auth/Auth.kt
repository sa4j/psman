/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.auth

/**
 * ユーザ認証
 */
interface Auth {

    /**
     * サインインを試行する
     */
    fun sign(password: String)

    /**
     * サインイン状態を取得する
     * @return true: サインイン済み  false: それ以外
     */
    fun hasSign(): Boolean

    /**
     * ログインを試行する
     */
    fun login(password: String): Boolean

    /**
     * ログイン状態を取得する
     * @return true: ログイン済み  false: それ以外
     */
    fun hasLogin(): Boolean

    /**
     * 暗号鍵を取得する
     */
    fun getCipherKeys(): Pair<ByteArray, ByteArray>

}