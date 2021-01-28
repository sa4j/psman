package com.sakibeko.psman

import com.sakibeko.psman.auth.PasswordEncoder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import javax.crypto.IllegalBlockSizeException

/**
 * 単体試験 for PasswordEncoder
 */
class UtPasswordEncoder {

    companion object {
        const val CORRECT_PASSWORD = "abcd1234"
        const val CORRECT_KEY1 = "1234567890123456"
        const val CORRECT_KEY2 = "abcdefghijklmnop"
        val TOO_LONG_PHRASE = "1234567890".repeat(100)
        val ENCODED_PASSWORD = PasswordEncoder.encode(
            CORRECT_PASSWORD, CORRECT_KEY1.toByteArray(), CORRECT_KEY2.toByteArray()
        )
    }

    @Test
    fun encodeCorrectly() {
        examineEncode()
    }

    @Test(expected = IllegalArgumentException::class)
    fun encodeWrongPassword() {
        examineEncode(password = "")
    }

    @Test(expected = IllegalArgumentException::class)
    fun encodeWrongKey1() {
        examineEncode(key1 = "")
    }

    @Test(expected = InvalidAlgorithmParameterException::class)
    fun encodeWrongKey2() {
        examineEncode(key2 = "")
    }

    @Test
    fun encodeTooLongPassword() {
        examineEncode(password = TOO_LONG_PHRASE)
    }

    @Test(expected = InvalidKeyException::class)
    fun encodeTooLongKey1() {
        examineEncode(key1 = TOO_LONG_PHRASE)
    }

    @Test(expected = InvalidAlgorithmParameterException::class)
    fun encodeTooLongKey2() {
        examineEncode(key2 = TOO_LONG_PHRASE)
    }

    @Test
    fun decodeCorrectly() {
        examineDecode()
    }

    @Test(expected = IllegalArgumentException::class)
    fun decodeWrongPassword() {
        examineDecode(encodedPassword = "")
    }

    @Test(expected = IllegalArgumentException::class)
    fun decodeWrongKey1() {
        examineDecode(key1 = "")
    }

    @Test(expected = InvalidAlgorithmParameterException::class)
    fun decodeWrongKey2() {
        examineDecode(key2 = "")
    }

    @Test(expected = IllegalBlockSizeException::class)
    fun decodeTooLongPassword() {
        examineDecode(encodedPassword = TOO_LONG_PHRASE)
    }

    @Test(expected = InvalidKeyException::class)
    fun decodeTooLongKey1() {
        examineDecode(key1 = TOO_LONG_PHRASE)
    }

    @Test(expected = InvalidAlgorithmParameterException::class)
    fun decodeTooLongKey2() {
        examineDecode(key2 = TOO_LONG_PHRASE)
    }

    /**
     * PasswordEncoder.encodeを試験する
     */
    private fun examineEncode(
        password: String = CORRECT_PASSWORD,
        key1: String = CORRECT_KEY1,
        key2: String = CORRECT_KEY2
    ) {
        val result = PasswordEncoder.encode(password, key1.toByteArray(), key2.toByteArray())
        assertNotEquals("Failed to encode password.", password, result)
    }

    /**
     * PasswordEncoder.decodeを試験する
     */
    private fun examineDecode(
        encodedPassword: String = ENCODED_PASSWORD,
        key1: String = CORRECT_KEY1,
        key2: String = CORRECT_KEY2
    ) {
        val result = PasswordEncoder.decode(encodedPassword, key1.toByteArray(), key2.toByteArray())
        assertEquals("Failed to decode password.", CORRECT_PASSWORD, result)
    }

}