package com.example.springbootwithkotlin.user.exception

import org.springframework.http.HttpStatus

class UserException(
    val status: HttpStatus,
    val code: String,
) : RuntimeException()