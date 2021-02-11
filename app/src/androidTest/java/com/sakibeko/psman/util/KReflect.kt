/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.util

import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * 対象のインスタンスからメンバ変数を取得する.
 * <p/>
 * 例）val context = getInstanceFieldAsAny(service, "mContext") as Context
 * @param target 対象のインスタンス
 * @param fieldName 取得したいメンバ変数の名称
 * @return 取得したいメンバ変数
 */
fun getInstanceField(target: Any, fieldName: String): Any? {
    val field = target::class.memberProperties.first() { it.name == fieldName }
    field.isAccessible = true
    return field.getter.call(target)
}