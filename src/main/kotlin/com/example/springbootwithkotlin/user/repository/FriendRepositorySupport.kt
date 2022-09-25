package com.example.springbootwithkotlin.user.repository

import com.example.springbootwithkotlin.user.constant.FriendAddStatus
import com.example.springbootwithkotlin.user.dto.FriendAcceptDto
import com.example.springbootwithkotlin.user.entity.FriendEntity
import com.example.springbootwithkotlin.user.entity.QFriendEntity.friendEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class FriendRepositorySupport(
    val jpaQueryFactory: JPAQueryFactory,
) : QuerydslRepositorySupport(FriendEntity::class.java) {
    fun findFriendRequestStatusPending(acceptor: Long): MutableList<FriendEntity> =
        jpaQueryFactory
            .selectFrom(friendEntity)
            .where(
                friendEntity.acceptor.eq(acceptor)
                    .and(friendEntity.status.eq(FriendAddStatus.PENDING))
            )
            .orderBy(friendEntity.createdAt.desc())
            .fetch()

    fun acceptFriendRequest(friendAcceptDto: FriendAcceptDto) =
        jpaQueryFactory
            .update(friendEntity)
            .set(friendEntity.status, FriendAddStatus.ACCEPTED)
            .where(
                friendEntity.id.eq(friendAcceptDto.requestId)
                    .and(friendEntity.acceptor.eq(friendAcceptDto.acceptor))
                    .and(friendEntity.requester.eq(friendAcceptDto.requester))
                    .and(friendEntity.status.eq(FriendAddStatus.PENDING))
            )
            .execute()
}
