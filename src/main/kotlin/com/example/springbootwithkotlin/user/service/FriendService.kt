package com.example.springbootwithkotlin.user.service

import com.example.springbootwithkotlin.user.constant.FriendAddStatus
import com.example.springbootwithkotlin.user.constant.UserResponseCode
import com.example.springbootwithkotlin.user.dto.FriendAcceptDto
import com.example.springbootwithkotlin.user.dto.FriendAddDto
import com.example.springbootwithkotlin.user.entity.FriendEntity
import com.example.springbootwithkotlin.user.exception.UserException
import com.example.springbootwithkotlin.user.repository.FriendRepository
import com.example.springbootwithkotlin.user.repository.FriendRepositorySupport
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FriendService(
    val friendRepository: FriendRepository,
    val friendRepositorySupport: FriendRepositorySupport,
) {

    @Transactional
    fun add(friendAddDto: FriendAddDto) {
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
