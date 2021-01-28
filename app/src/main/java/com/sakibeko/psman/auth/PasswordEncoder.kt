/*
 * Copyright 2021 sakibeko
 */
package com.sakibeko.psman.auth

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * パスワードの変換（符号化・復号化）
 */
class PasswordEncoder {
    companion object {
        /**
         * パスワードの符号化
         */
        fun encode(password: String, key: ByteArray, iv: ByteArray): String {
            if (password == "") {
                throw IllegalArgumentException("password must not be blank.")
            }

            val cipherKey = SecretKeySpec(key, CIPHER_ALGORITHM)
            val initVector = IvParameterSpec(iv)
            val cipher = Cipher.getInstance("$CIPHER_ALGORITHM/$CIPHER_MODE/$CIPHER_PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, cipherKey, initVector)
            val result = cipher.doFinal(password.toByteArray())
            return String(Base64.getEncoder().encode(result))
        }

        /**
         * パスワードの復号化
         */
        fun decode(password: String, key: ByteArray, iv: ByteArray): String {
            if (password == "") {
                throw IllegalArgumentException("password must not be blank.")
            }

            val cipherKey = SecretKeySpec(key, CIPHER_ALGORITHM)
            val initVector = IvParameterSpec(iv)
            val cipher = Cipher.getInstance("$CIPHER_ALGORITHM/$CIPHER_MODE/$CIPHER_PADDING")
            cipher.init(Cipher.DECRYPT_MODE, cipherKey, initVector)
            val result = Base64.getDecoder().decode(password.toByteArray())
            return String(cipher.doFinal(result))
        }
    }
}

/** 暗号化アルゴリズム */
private const val CIPHER_ALGORITHM = "AES"

/** 暗号化モード */
private const val CIPHER_MODE = "CBC"

/** 暗号化パディング方式 */
private const val CIPHER_PADDING = "PKCS5Padding"
