package com.example.springbootwithkotlin.user.repository

import com.example.springbootwithkotlin.user.constant.FriendAddStatus
import com.example.springbootwithkotlin.user.entity.FriendEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FriendRepository : JpaRepository<FriendEntity, Long> {
    fun findByAcceptorAndStatusOrderByCreatedAtDesc(acceptor: Long, status: FriendAddStatus): List<FriendEntity>
}
