/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.auth

import android.content.Context
import com.sakibeko.psman.auth.data.MasterKeyRepository
import com.sakibeko.psman.auth.data.source.DefaultMasterKeyRepository
import java.security.MessageDigest

/**
 * ユーザ認証（実装）
 */
class AuthImpl(mContext: Context) : Auth {

    /**
     * ログイン状態
     * true: ログイン済み  false: それ以外
     */
    private var mHasLogin = false

    /** マスターキーの管理API */
    private val mRepository: MasterKeyRepository = DefaultMasterKeyRepository(mContext)

    /**
     * メッセージダイジェスト
     */
    private var mDigest: String = ""


    /**
     * サインインを試行する
     */
    override fun sign(password: String) {
        val digest = provideDigest(password)
        mRepository.save(digest)
    }

    /**
     * サインイン状態を取得する
     * @return true: サインイン済み  false: それ以外
     */
    override fun hasSign(): Boolean = mRepository.hasSaved()

    /**
     * ログインを試行する
     */
    override fun login(password: String): Boolean {
        if (!hasSign()) {
            return false
        }

        val digest = provideDigest(password)
        mHasLogin = mRepository.isEqual(digest)

        if (mHasLogin) {
            mDigest = digest
        }

        return mHasLogin
    }

    /**
     * ログイン状態を取得する
     * @return true: ログイン済み  false: それ以外
     */
    override fun hasLogin(): Boolean {
        return mHasLogin
    }

    /**
     * 暗号鍵を取得する
     */
    override fun getCipherKeys(): Pair<ByteArray, ByteArray> {
        if (mDigest.length < DIGEST_SIZE) {
            throw IllegalStateException("User is not logged in.")
        }

        // 数字に特別な意図はない
        val key1 = mDigest.substring(0, 16).toByteArray()
        val key2 = mDigest.substring(16, 32).toByteArray()
        return Pair(key1, key2)
    }

    /**
     * メッセージダイジェストを生成する
     */
    private fun provideDigest(password: String): String {
        val digest = MessageDigest.getInstance(DIGEST_ALGORITHM).digest(password.toByteArray())

        // 16進数を文字列に変換する ex)0xf3 -> "f3"
        // 1バイトの前半と後半で2文字を抽出可能であるため、2倍の領域を確保する
        val result = StringBuilder(digest.size * 2)
        digest.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }
        return result.toString()
    }

}

/** メッセージダイジェストの生成アルゴリズム */
private const val DIGEST_ALGORITHM = "SHA-256"

/** メッセージダイジェストのサイズ */
private const val DIGEST_SIZE = 64

/** メッセージダイジェストを文字列に変換する際に使用する */
private const val HEX_CHARS = "0123456789abcdef"
