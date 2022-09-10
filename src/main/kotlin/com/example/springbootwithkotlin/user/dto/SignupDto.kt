package com.example.springbootwithkotlin.user.dto

import com.example.springbootwithkotlin.user.util.EmailUtil
import org.hibernate.validator.constraints.Length
import javax.validation.GroupSequence
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.groups.Default

data class SignupDto(
    @field:Length(
        message = "{validation.signup.name.too_short}",
        groups = [ValidationGroups.Length::class],
        min = 2,
    )
    @field:Pattern(
        message = "{validation.signup.name.invalid_char}",
        groups = [ValidationGroups.Pattern::class],
        regexp = """[a-zA-Z가-힣]+"""
    )
    val name: String,

    @field:NotBlank(
        message = "{validation.signup.email.not_blank}",
        groups = [ValidationGroups.NotBlank::class]
    )
    @field:Email(
        message = "{validation.signup.email.invalid_format}",
        regexp = """^[a-zA-Z0-9_!#${'$'}%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+${'$'}""",
        groups = [ValidationGroups.PatternCheck::class]
    )
    val email: String,

    @field:Length(
        message = "{validation.signup.password.invalid_length}",
        groups = [ValidationGroups.Length::class],
        min = 6,
        max = 12
    )
    @field:Pattern(
        message = "{validation.signup.password.invalid_pattern}",
        groups = [ValidationGroups.Pattern::class],
        regexp = """[\x21-\x7E]+""",
    )
    val password: String,

    @field:NotBlank(
        message = "{validation.signup.phone_number.not_blank}",
        groups = [ValidationGroups.NotBlank::class]
    )
    @field:Pattern(
        message = "{validation.signup.phone_number.only_number}",
        groups = [ValidationGroups.Pattern::class],
        regexp = """[0-9]+"""
    )
    val phoneNumber: String,
) {
    init {
        isValidDomain()
    }

    @AssertTrue(
        message = "{validation.signup.email.invalid_domain}",
        groups = [ValidationGroups.AssertDomain::class]
    )
    fun isValidDomain() = EmailUtil.isValidDomain(email)

    @GroupSequence(
        Default::class,
        ValidationGroups.NotBlank::class,
        ValidationGroups.Length::class,
        ValidationGroups.PatternCheck::class,
        ValidationGroups.AssertDomain::class,
        ValidationGroups.Pattern::class,
    )

    interface ValidationSequence

    class ValidationGroups {
        interface NotBlank
        interface Length
        interface PatternCheck
        interface AssertDomain
        interface Pattern
    }
}
