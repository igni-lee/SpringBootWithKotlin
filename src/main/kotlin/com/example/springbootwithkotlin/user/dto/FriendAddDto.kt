package com.example.springbootwithkotlin.user.dto

data class FriendAddDto(
    val requesterId: Long,
    val acceptorId: Long,
)
