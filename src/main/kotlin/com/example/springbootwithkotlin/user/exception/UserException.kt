package com.example.springbootwithkotlin.user.exception

import com.example.springbootwithkotlin.common.exception.GlobalException
import org.springframework.http.HttpStatus

class UserException(
    httpStatus: HttpStatus,
    errorCode: String,
    i18nCode: String,
) : GlobalException(httpStatus, errorCode, i18nCode) {
    constructor(exceptionCode: UserExceptionCode) : this(
        httpStatus = exceptionCode.httpStatus,
        errorCode = exceptionCode.errorCode,
        i18nCode = exceptionCode.i18nCode
    )
}
