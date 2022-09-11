package com.example.springbootwithkotlin.user.dto

import org.hibernate.validator.constraints.Length
import javax.validation.GroupSequence
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.groups.Default

data class LoginDto(
    @field:NotBlank(
        message = "{validation.login.email.not_blank}",
        groups = [ValidationGroups.NotBlank::class]
    )
    @field:Email(
        message = "{validation.login.email.invalid_format}",
        regexp = """^[a-zA-Z0-9_!#${'$'}%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+${'$'}""",
        groups = [ValidationGroups.PatternCheck::class]
    )
    val email: String,

    @field:Length(
        message = "{validation.login.password.invalid_length}",
        groups = [ValidationGroups.Length::class],
        min = 6,
        max = 12
    )
    @field:Pattern(
        message = "{validation.login.password.invalid_pattern}",
        groups = [ValidationGroups.Pattern::class],
        regexp = """[\x21-\x7E]+""",
    )
    val password: String,
) {
    @GroupSequence(
        Default::class,
        ValidationGroups.NotBlank::class,
        ValidationGroups.Length::class,
        ValidationGroups.PatternCheck::class,
        ValidationGroups.Pattern::class,
    )

    interface ValidationSequence

    class ValidationGroups {
        interface NotBlank
        interface Length
        interface PatternCheck
        interface Pattern
    }
}
