/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user.data.source

import androidx.lifecycle.LiveData
import com.sakibeko.psman.user.data.User
import com.sakibeko.psman.user.data.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ユーザ情報の管理API(ローカルストレージ)
 */
class DefaultUserRepository(
    private val mUserDao: UserDao, private val mIoDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    /** ユーザ情報の全取得 */
    override fun all(): LiveData<List<User>> = mUserDao.all()

    /** ごみ箱の全取得 */
    override fun allTrash(): LiveData<List<User>> = mUserDao.trashAll()

    /** ユーザ情報の取得 */
    override suspend fun getUser(id: Int): User = withContext(mIoDispatcher) {
        return@withContext mUserDao.getUser(id)
    }

    /** ユーザ情報の保存 */
    override suspend fun save(user: User) = withContext(mIoDispatcher) {
        mUserDao.save(user)
    }

    /** ゴミ箱の削除 */
    override suspend fun emptyTrash() = withContext(mIoDispatcher) {
        mUserDao.emptyTrash()
    }

}