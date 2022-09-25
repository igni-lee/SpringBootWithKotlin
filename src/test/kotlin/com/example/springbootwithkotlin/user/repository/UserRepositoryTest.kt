package com.example.springbootwithkotlin.user.repository

import com.example.springbootwithkotlin.fixture.Fixture
import com.example.springbootwithkotlin.user.util.CryptUtil
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("NonAsciiCharacters")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest(
    @Autowired
    val fixture: Fixture,
    @Autowired
    val userRepository: UserRepository,
) {
    @BeforeEach
    fun beforeEach() {
        fixture.initData()
    }

    @Test
    fun `id로 사용자를 조회할 수 있다`() {
        val basicUser = userRepository.findById(1).orElse(null)

        Assertions.assertNotNull(basicUser)
        Assertions.assertEquals("test@test.com", basicUser.email)
        Assertions.assertEquals(basicUser.password, CryptUtil.crypt("password", basicUser.passwordSalt))
    }
}
