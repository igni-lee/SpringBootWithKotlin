package com.example.springbootwithkotlin.user.exception

import org.springframework.http.HttpStatus

class SignupException(
    val status: HttpStatus,
    val code: String,
) : RuntimeException()