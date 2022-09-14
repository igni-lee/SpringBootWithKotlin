package com.example.springbootwithkotlin.common.exception

import org.springframework.http.HttpStatus

abstract class GlobalException(
    val httpStatus: HttpStatus,
    val errorCode: String,
    val i18nCode: String,
): RuntimeException() {
}