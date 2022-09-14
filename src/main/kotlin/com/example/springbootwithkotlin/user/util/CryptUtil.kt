package com.example.springbootwithkotlin.user.util

import org.apache.commons.codec.digest.Crypt

class CryptUtil {
    companion object {
        fun crypt(key: String, salt: String): String = Crypt.crypt(key, "\$6\$$salt")
    }
}
