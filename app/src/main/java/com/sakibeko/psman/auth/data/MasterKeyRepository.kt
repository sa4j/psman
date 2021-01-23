/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.auth.data

/**
 * マスターキーの管理API
 */
interface MasterKeyRepository {

    /**
     * マスターキーの保存
     */
    fun save(masterKey: String)

    /**
     * マスターキーの保存済み判定
     * @return true: 保存済み  false: それ以外
     */
    fun hasSaved(): Boolean

    /**
     * マスターキーの一致判定
     * @return true: 一致する  false: それ以外
     */
    fun isEqual(masterKey: String): Boolean

}