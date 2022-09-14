package com.example.springbootwithkotlin.user.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters")
class EmailUtilTest {

    @Test
    fun `유효하지 않은 도메인을 식별할 수 있다`() {
        Assertions.assertTrue(EmailUtil.isValidDomain("igni@gmail.com"))
        Assertions.assertFalse(EmailUtil.isValidDomain("igni@klasfdhjkljashdfklajhs.com"))
    }
}
