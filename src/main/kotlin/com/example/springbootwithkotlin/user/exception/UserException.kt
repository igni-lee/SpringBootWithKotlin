package com.example.springbootwithkotlin.user.exception

import com.example.springbootwithkotlin.common.exception.GlobalException
import com.example.springbootwithkotlin.user.constant.UserResponseCode
import org.springframework.http.HttpStatus

class UserException(
    httpStatus: HttpStatus,
    errorCode: String,
    i18nCode: String,
) : GlobalException(httpStatus, errorCode, i18nCode) {
    constructor(userResponseCode: UserResponseCode) : this(
        httpStatus = userResponseCode.httpStatus,
        errorCode = userResponseCode.name,
        i18nCode = userResponseCode.i18nCode
    )
}
