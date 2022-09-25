package com.example.springbootwithkotlin.user.constant

import org.springframework.http.HttpStatus

enum class UserResponseCode(val httpStatus: HttpStatus, val i18nCode: String) {
    SUCCESS(HttpStatus.OK, ""),

    // Validation
    INVALID(HttpStatus.BAD_REQUEST, ""),
    INVALID_NAME(HttpStatus.BAD_REQUEST, ""),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, ""),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, ""),
    INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, ""),

    // Signup
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "validation.signup.email.duplicate"),

    // Login
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "login.fail"),

    // Search
    USER_NOT_FOUND(HttpStatus.OK, "search.user.not_found"),

    // Friend
    NOT_EXIST_FRIEND_REQUEST(HttpStatus.NO_CONTENT, "friend.not_exist_request"),
}
