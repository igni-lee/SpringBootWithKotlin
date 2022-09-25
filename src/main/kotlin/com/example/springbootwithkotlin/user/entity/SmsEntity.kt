package com.example.springbootwithkotlin.user.entity

import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "sms", uniqueConstraints = [UniqueConstraint(name = "uk_sms_request_id", columnNames = ["request_id"])])
class SmsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom")
    @GenericGenerator(name = "custom", strategy = "com.example.springbootwithkotlin.common.entity.IdOrGenerate")
    var id: Long? = null,

    @Column(name = "from_number", nullable = false)
    var fromNumber: String,

    @Column(name = "to_number", nullable = false)
    var toNumber: String,

    @Column(name = "auth_number", nullable = true)
    var authNumber: String? = null,

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "request_id", nullable = true)
    var requestId: String? = null,

    @Column(name = "request_time", nullable = true)
    var requestTime: LocalDateTime? = null,

    @Column(name = "status_code", nullable = true)
    var statusCode: Int? = null,

    @Column(name = "status_name", nullable = true)
    var statusName: String? = null,

    @Column(name = "is_used", nullable = false)
    var isUsed: Boolean = false,
)
