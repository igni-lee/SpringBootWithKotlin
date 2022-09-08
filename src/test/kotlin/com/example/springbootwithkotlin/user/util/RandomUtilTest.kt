package com.example.springbootwithkotlin.user.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters")
class RandomUtilTest {
    @Test
    fun `난수는 기본 여섯자리숫자이다`() {
        Assertions.assertEquals(6, RandomUtil.generateRandomNumber().length)
    }

    @Test
    fun `난수 자리수를 지정할 수 있다`() {
        Assertions.assertEquals(8, RandomUtil.generateRandomNumber(8).length)
    }
}