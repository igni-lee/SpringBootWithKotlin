package com.example.springbootwithkotlin.user.repository

import com.example.springbootwithkotlin.fixture.Fixture
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositorySupportTest(
    @Autowired
    val fixture: Fixture,
    @Autowired
    val userRepositorySupport: UserRepositorySupport,
) {
    val basicUser = Fixture.User.basicUser

    @BeforeEach
    fun beforeEach() = fixture.initData()

    @Test
    fun findByEmailTest() {
        val user = userRepositorySupport.findByEmail(basicUser.email)

        Assertions.assertNotNull(user)

        Assertions.assertEquals(basicUser.id, user!!.id)
        Assertions.assertEquals(basicUser.password, user.password)
        Assertions.assertEquals(basicUser.email, user.email)
        Assertions.assertEquals(basicUser.passwordSalt, user.passwordSalt)
        Assertions.assertEquals(basicUser.name, user.name)
        Assertions.assertEquals(basicUser.phoneNumber, user.phoneNumber)
    }
}
