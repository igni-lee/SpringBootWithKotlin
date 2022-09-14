package com.example.springbootwithkotlin.user.util

class RandomUtil {
    companion object {
        fun generateRandomNumber(length: Int = 6) =
            (1..length)
                .map { ('0'..'9').random() }
                .joinToString("")

        fun generateSalt(length: Int = 8) =
            (1..length)
                .map { (('A'..'Z') + ('a'..'z') + ('0'..'9')).random() }
                .joinToString("")
    }
}
