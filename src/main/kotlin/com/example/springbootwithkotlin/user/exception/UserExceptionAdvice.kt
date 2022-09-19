package com.example.springbootwithkotlin.user.exception

import com.example.springbootwithkotlin.common.response.Error
import com.example.springbootwithkotlin.user.constant.UserResponseCode
import com.example.springbootwithkotlin.user.controller.UserController
import mu.KLogging
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(1)
@RestControllerAdvice(assignableTypes = [UserController::class])
class UserExceptionAdvice(
    val messageSource: MessageSource,
) {

    companion object : KLogging()

    @ExceptionHandler(value = [BindException::class])
    fun bindExceptionHandler(ex: BindException): ResponseEntity<Error> {
        logger.debug("Exception ${ex.javaClass.simpleName} : ${ex.message}")
        logger.debug("codes : ${ex.bindingResult.allErrors.first().codes}")

        val error = ex.bindingResult.allErrors.first()

        val code = when (error.codes?.first()) {
            // NAME
            "Length.signupDto.name",
            "Pattern.signupDto.name",
            -> UserResponseCode.INVALID_NAME.name
            // EMAIL
            "NotBlank.signupDto.email",
            "Email.signupDto.email",
            "AssertTrue.signupDto.validDomain",
            "NotBlank.loginDto.email",
            "Email.loginDto.email",
            -> UserResponseCode.INVALID_EMAIL.name
            // PASSWORD
            "Length.signupDto.password",
            "Pattern.signupDto.password",
            "Length.loginDto.password",
            "Pattern.loginDto.password",
            -> UserResponseCode.INVALID_PASSWORD.name
            // PHONE NUMBER
            "NotBlank.signupDto.phoneNumber",
            "Pattern.signupDto.phoneNumber"
            -> UserResponseCode.INVALID_PHONE_NUMBER.name

            else -> UserResponseCode.INVALID.name
        }

        return ResponseEntity
            .badRequest()
            .body(
                Error(
                    code = code,
                    message = error.defaultMessage!!
                )
            )
    }

    @ExceptionHandler(value = [UserException::class])
    fun userException(ex: UserException): ResponseEntity<Error> = ResponseEntity(
        Error(
            code = ex.errorCode,
            message = messageSource.getMessage(
                ex.i18nCode,
                arrayOf(),
                LocaleContextHolder.getLocale(),
            )
        ),
        ex.httpStatus,
    )
}
