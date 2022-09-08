package com.example.springbootwithkotlin.user.util

import org.apache.commons.codec.digest.Crypt

class CryptUtil {
    companion object {
        fun crypt(key: String, salt: String): String = Crypt.crypt(key, "\$6\$$salt")

        fun generateSalt(length: Int = 8) =
            (1..length)
                .map { (('A'..'Z') + ('a'..'z') + ('0'..'9')).random() }
                .joinToString("")


    }
}