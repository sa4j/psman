/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.util

import androidx.fragment.app.Fragment
import com.sakibeko.psman.util.viewmodel.ViewModelFactory

/**
 * ViewModelの生成
 */
fun Fragment.getViewModelFactory(): ViewModelFactory {
    // Application
    val myApplication = (requireContext().applicationContext as MyApplication)
    // ViewModel生成
    return ViewModelFactory(myApplication, this)
}
