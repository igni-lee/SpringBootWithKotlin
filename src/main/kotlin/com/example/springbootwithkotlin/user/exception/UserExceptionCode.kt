package com.example.springbootwithkotlin.user.exception

import org.springframework.http.HttpStatus

enum class UserExceptionCode(
    val httpStatus: HttpStatus,
    val errorCode: String,
    val i18nCode: String
) {
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "email_duplicate", "validation.signup.email.duplicate"),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "login_fail","login.fail"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user_not_found", "search.user.not_found")
}