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
                name = "이그니",
                email = "test@test.com",
                phoneNumber = "01012341234",
                password = "\$6\$sMeo1Fh5\$Sr9svHnvb/ThyNceUn0AHqIgNHQVEXKklN3TBk9Et/QzhpHl11OHtCJM9d6aRQXQCMvhx66RrQl0IuSHw0nyx/",
                passwordSalt = "sMeo1Fh5"
            )

            val list = listOf(basicUser)
        }
    }

    fun initData() {
        User.list.map { userRepository.save(it) }
    }
}
