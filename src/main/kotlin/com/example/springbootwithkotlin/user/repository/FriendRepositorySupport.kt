package com.example.springbootwithkotlin.user.repository

import com.example.springbootwithkotlin.user.constant.FriendAddStatus
import com.example.springbootwithkotlin.user.entity.FriendEntity
import com.example.springbootwithkotlin.user.entity.QFriendEntity.friendEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class FriendRepositorySupport(
    val jpaQueryFactory: JPAQueryFactory,
) : QuerydslRepositorySupport(FriendEntity::class.java) {
    fun findFriendRequestStatusPending(acceptor: Long): MutableList<FriendEntity>? {
        val result = jpaQueryFactory
            .selectFrom(friendEntity)
            .where(
                friendEntity.acceptor.eq(acceptor)
                    .and(friendEntity.status.eq(FriendAddStatus.PENDING))
            )
            .orderBy(friendEntity.createdAt.desc())
            .fetch()

        println(result)
        return result
    }
}
