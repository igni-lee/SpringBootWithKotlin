package com.example.springbootwithkotlin.user.service

import com.example.springbootwithkotlin.user.constant.FriendAddStatus
import com.example.springbootwithkotlin.user.dto.FriendAddDto
import com.example.springbootwithkotlin.user.entity.FriendEntity
import com.example.springbootwithkotlin.user.repository.FriendRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FriendService(
    val friendRepository: FriendRepository,
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
    fun requestList(acceptor: Long) = friendRepository.findByAcceptorAndStatusOrderByCreatedAtDesc(acceptor, FriendAddStatus.PENDING)

}
