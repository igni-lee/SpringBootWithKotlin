package com.example.springbootwithkotlin.user.dto

data class FriendAcceptDto(
    val requestId: Long,
    val acceptor: Long,
    val requester: Long,
)
