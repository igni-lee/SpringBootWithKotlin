package com.example.springbootwithkotlin.user.dto

import java.time.LocalDateTime

data class SmsResponseDto(
    val requestId: String,
    val requestTime: LocalDateTime?,
    val statusCode: String,
    val statusName: String,
) {
    constructor(): this(
        "",
        null,
        "",
        "",
    )
}
