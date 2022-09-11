package com.example.springbootwithkotlin.user.service

import com.example.springbootwithkotlin.user.dto.LoginDto
import com.example.springbootwithkotlin.user.dto.SignupDto
import com.example.springbootwithkotlin.user.entity.UserEntity
import com.example.springbootwithkotlin.user.exception.LoginException
import com.example.springbootwithkotlin.user.exception.SignupException
import com.example.springbootwithkotlin.user.repository.UserRepository
import com.example.springbootwithkotlin.user.util.CryptUtil
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    val userRepository: UserRepository,
) {
    @Transactional
    fun signup(signupDto: SignupDto) {
        userRepository.findByEmail(signupDto.email).ifPresent {
            throw SignupException(HttpStatus.CONFLICT, "email_duplicate")
        }

        userRepository.save(UserEntity.from(signupDto))
    }

    fun login(loginDto: LoginDto) {
        val user = userRepository.findByEmail(loginDto.email).orElse(null) ?: throw LoginException(
            HttpStatus.NOT_FOUND,
            code = "validation.login.email.not_found"
        )

        if (user.password != CryptUtil.crypt(loginDto.password, user.passwordSalt)) {
            throw LoginException(HttpStatus.BAD_REQUEST, code = "validation.login.password.not_match")
        }
    }
}