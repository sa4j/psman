/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.sakibeko.psman.R

/**
 * Activity:ユーザ管理画面
 */
class UserEditActivity : AppCompatActivity() {

    /** ナビゲーションメニューの設定情報 */
    private lateinit var mAppBarConfiguration: AppBarConfiguration

    /** ナビゲーションメニューの表示領域 */
    private lateinit var mDrawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)

        setupNavigationDrawer()
        setSupportActionBar(findViewById(R.id.toolbar))
        setupToolbar()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(mAppBarConfiguration) ||
                super.onSupportNavigateUp()
    }

    /**
     * ナビゲーションメニューの表示領域の初期化
     */
    private fun setupNavigationDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout)
    }

    /**
     * ツールバーの初期化
     */
    private fun setupToolbar() {
        // ナビゲーションメニューの設定情報の生成
        mAppBarConfiguration = AppBarConfiguration.Builder(R.id.dest_user_list)
            .setOpenableLayout(mDrawerLayout).build()
        // アクションバーにナビゲーションサポートを追加する
        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, mAppBarConfiguration)
        // navControllerとUIをひもづける
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)
    }

}