package com.example.springbootwithkotlin.user.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CryptUtilTest {
    @Test
    fun password() {
        Assertions.assertEquals(
            "\$6\$sMeo1Fh5\$Sr9svHnvb/ThyNceUn0AHqIgNHQVEXKklN3TBk9Et/QzhpHl11OHtCJM9d6aRQXQCMvhx66RrQl0IuSHw0nyx/",
            CryptUtil.crypt("password", "sMeo1Fh5")
        )
    }
}
