/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.auth.data.source

import android.content.Context
import android.content.SharedPreferences
import com.sakibeko.psman.auth.data.MasterKeyRepository

/**
 * マスターキーの管理API（ローカルストレージ）
 */
class DefaultMasterKeyRepository(mContext: Context) : MasterKeyRepository {

    private val mPref: SharedPreferences =
        mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val mEditor: SharedPreferences.Editor = mPref.edit()

    /**
     * マスターキーの保存
     */
    override fun save(masterKey: String) {
        mEditor.putString(DATA_NAME, masterKey).commit()
    }

    /**
     * マスターキーの保存済み判定
     * @return true: 保存済み  false: それ以外
     */
    override fun hasSaved(): Boolean {
        return mPref.getString(DATA_NAME, "") !== ""
    }

    /**
     * マスターキーの一致判定
     * @return true: 一致する  false: それ以外
     */
    override fun isEqual(masterKey: String): Boolean {
        return masterKey == mPref.getString(DATA_NAME, "")
    }

}

private const val PREF_NAME = "master_key"
private const val DATA_NAME = PREF_NAME