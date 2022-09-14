package com.example.springbootwithkotlin.user.entity

import com.example.springbootwithkotlin.common.entity.BaseEntity
import com.example.springbootwithkotlin.user.constant.FriendAddStatus
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "friend")
class FriendEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom")
    var id: Long? = null,

    @Column(name = "requester", nullable = false)
    var requester: Long,

    @Column(name = "acceptor", nullable = false)
    var acceptor: Long,

    @Column(name = "status", nullable = false)
    var status: FriendAddStatus, // PENDING , REJECT, ACCEPTED
) : BaseEntity()
