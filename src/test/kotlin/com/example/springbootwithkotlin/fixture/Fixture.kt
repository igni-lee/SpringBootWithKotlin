package com.example.springbootwithkotlin.fixture

import com.example.springbootwithkotlin.user.constant.FriendAddStatus
import com.example.springbootwithkotlin.user.entity.FriendEntity
import com.example.springbootwithkotlin.user.entity.UserEntity
import com.example.springbootwithkotlin.user.repository.FriendRepository
import com.example.springbootwithkotlin.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Fixture(
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val friendRepository: FriendRepository,
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
            val friendRequestAcceptUser = UserEntity(
                id = 2,
                name = "많관부",
                email = "friend@test.com",
                phoneNumber = "01012341234",
                password = "\$6\$sMeo1Fh5\$Sr9svHnvb/ThyNceUn0AHqIgNHQVEXKklN3TBk9Et/QzhpHl11OHtCJM9d6aRQXQCMvhx66RrQl0IuSHw0nyx/",
                passwordSalt = "sMeo1Fh5"
            )

            val list = listOf(basicUser, friendRequestAcceptUser)
        }
    }

    class Friend {
        companion object {
            val friendRequest2 = FriendEntity(
                id = 1,
                requester = 2,
                acceptor = 1,
                status = FriendAddStatus.PENDING,
            )

            val friendRequest3 = FriendEntity(
                id = 2,
                requester = 3,
                acceptor = 1,
                status = FriendAddStatus.PENDING,
            )

            val friendRequest4 = FriendEntity(
                id = 3,
                requester = 4,
                acceptor = 1,
                status = FriendAddStatus.PENDING,
            )

            val friendRequest5 = FriendEntity(
                id = 4,
                requester = 5,
                acceptor = 1,
                status = FriendAddStatus.PENDING,
            )

            val list = listOf(friendRequest2, friendRequest3, friendRequest4, friendRequest5)
        }
    }

    fun initData() {
        userRepository.deleteAll()
        friendRepository.deleteAll()

        User.list.map { userRepository.save(it) }
        Friend.list.map { friendRepository.save(it) }
    }
}
