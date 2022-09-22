package com.example.springbootwithkotlin.common.response

import com.example.springbootwithkotlin.user.constant.UserResponseCode

data class ResponseData<T>(
    val code: String = UserResponseCode.SUCCESS.name,
    val data: T? = null,
)
