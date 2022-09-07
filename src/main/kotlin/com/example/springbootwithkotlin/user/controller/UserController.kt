package com.example.springbootwithkotlin.user.controller

import com.example.springbootwithkotlin.user.dto.SignupDto
import com.example.springbootwithkotlin.user.service.UserService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    val userService: UserService,
) {
    @PostMapping
    fun signup(
        @RequestBody
        @Validated(*[SignupDto.ValidationSequence::class])
        signupDto: SignupDto
    ) = userService.signup(signupDto)
}