package com.example.springbootwithkotlin.user.repository

import com.example.springbootwithkotlin.user.entity.QUserEntity.userEntity
import com.example.springbootwithkotlin.user.entity.UserEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class UserRepositorySupport(
    val jpaQueryFactory: JPAQueryFactory,
) : QuerydslRepositorySupport(UserEntity::class.java) {

    fun findByEmail(email: String): UserEntity? {
        return jpaQueryFactory
            .selectFrom(userEntity)
            .where(userEntity.email.eq(email))
            .fetchOne()
    }
}
