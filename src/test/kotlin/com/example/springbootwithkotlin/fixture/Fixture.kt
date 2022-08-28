package com.example.springbootwithkotlin.fixture

import com.example.springbootwithkotlin.user.entity.UserEntity
import com.example.springbootwithkotlin.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Fixture(
    @Autowired
    val userRepository: UserRepository,
) {
    class User {
        companion object {
            val basicUser = UserEntity(
                id = 1,
                email = "test@test.com",
                password = "password",
            )

            val list = listOf(basicUser)
        }

    }

    fun initData() {
        User.list.map { userRepository.save(it) }
    }
}