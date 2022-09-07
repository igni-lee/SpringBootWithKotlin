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
    @field:Length(min = 2, message = "이름은 두글자 이상이어야 합니다.")
    @field:Pattern(
        message = "이름에 들어갈 수 없는 문자가 있습니다.",
        groups = [ValidationGroups.Pattern::class],
        regexp = """[a-zA-Z가-힣]+"""
    )
    val name: String,

    @field:NotBlank(
        message = "이메일은 공백일 수 없습니다.",
        groups = [ValidationGroups.NotBlank::class]
    )
    @field:Email(
        regexp = """^[a-zA-Z0-9_!#${'$'}%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+${'$'}""",
        message = "올바른 이메일 형식이 아닙니다.",
        groups = [ValidationGroups.PatternCheck::class]
    )
    val email: String,

    @field:Length(min = 6, max = 12, message = "비밀번호는 6자 이상, 12자 이하여야합니다.")
    @field:Pattern(
        message = "비밀번호에 포함할 수 없는 특수문자가 있습니다.",
        groups = [ValidationGroups.Pattern::class],
        regexp = """[\x21-\x7E]+""",
    )
    val password: String,

    @field:NotBlank(
        message = "휴대폰 번호는 공백일 수 없습니다.",
        groups = [ValidationGroups.NotBlank::class]
    )
    @field:Pattern(
        message = "휴대폰 번호를 확인해주세요.",
        groups = [ValidationGroups.Pattern::class],
        regexp = """[0-9]+"""
    )
    val phoneNumber: String,
){
    init {
        isValidDomain()
    }

    @AssertTrue(
        message = "유효하지 않은 도메인입니다.",
        groups = [ValidationGroups.AssertDomain::class]
    )
    fun isValidDomain() = EmailUtil.isValidDomain(email)

    @GroupSequence(
        Default::class,
        ValidationGroups.NotBlank::class,
        ValidationGroups.PatternCheck::class,
        ValidationGroups.AssertDomain::class,
        ValidationGroups.Pattern::class,
    )

    interface ValidationSequence

    class ValidationGroups {
        interface NotBlank
        interface PatternCheck
        interface AssertDomain
        interface Pattern
    }
}
