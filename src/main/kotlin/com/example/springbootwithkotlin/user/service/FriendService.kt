package com.example.springbootwithkotlin.user.service

import com.example.springbootwithkotlin.user.constant.FriendAddStatus
import com.example.springbootwithkotlin.user.constant.UserResponseCode
import com.example.springbootwithkotlin.user.dto.FriendAcceptDto
import com.example.springbootwithkotlin.user.dto.FriendAddDto
import com.example.springbootwithkotlin.user.entity.FriendEntity
import com.example.springbootwithkotlin.user.exception.UserException
import com.example.springbootwithkotlin.user.repository.FriendRepository
import com.example.springbootwithkotlin.user.repository.FriendRepositorySupport
import com.example.springbootwithkotlin.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FriendService(
    val friendRepository: FriendRepository,
    val friendRepositorySupport: FriendRepositorySupport,
    val userRepository: UserRepository,
) {

    @Transactional
    fun add(friendAddDto: FriendAddDto) {
        userRepository.findById(friendAddDto.acceptorId).orElseThrow {
            throw UserException(UserResponseCode.USER_NOT_FOUND)
        }

        val friendEntity = FriendEntity(
            requester = friendAddDto.requesterId,
            acceptor = friendAddDto.acceptorId,
            status = FriendAddStatus.PENDING,
        )

        friendRepository.save(friendEntity)
    }

    @Transactional
    fun friendRequestList(acceptor: Long) =
        friendRepositorySupport.findFriendRequestStatusPending(acceptor)

    @Transactional
    fun accept(friendAcceptDto: FriendAcceptDto) {
        val result = friendRepositorySupport.acceptFriendRequest(friendAcceptDto)

        if (result != 1L) throw UserException(UserResponseCode.NOT_EXIST_FRIEND_REQUEST)
    }
}
