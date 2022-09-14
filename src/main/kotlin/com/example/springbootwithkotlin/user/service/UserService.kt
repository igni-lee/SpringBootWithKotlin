package com.example.springbootwithkotlin.user.service

import com.example.springbootwithkotlin.user.dto.LoginDto
import com.example.springbootwithkotlin.user.dto.SignupDto
import com.example.springbootwithkotlin.user.dto.UserInfoDto
import com.example.springbootwithkotlin.user.entity.UserEntity
import com.example.springbootwithkotlin.user.exception.UserException
import com.example.springbootwithkotlin.user.exception.UserExceptionCode
import com.example.springbootwithkotlin.user.repository.UserRepository
import com.example.springbootwithkotlin.user.util.CryptUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    val userRepository: UserRepository,
) {
    @Transactional
    fun signup(signupDto: SignupDto) {
        userRepository.findByEmail(signupDto.email).ifPresent {
            throw UserException(UserExceptionCode.EMAIL_DUPLICATE)
        }

        userRepository.save(UserEntity.from(signupDto))
    }

    fun login(loginDto: LoginDto) {
        val user = userRepository.findByEmail(loginDto.email).orElse(null) ?: throw UserException(UserExceptionCode.LOGIN_FAIL)

        if (user.password != CryptUtil.crypt(loginDto.password, user.passwordSalt)) {
            throw UserException(UserExceptionCode.LOGIN_FAIL)
        }
    }

    fun searchByEmail(email: String): UserInfoDto {
        val user =
            userRepository.findByEmail(email).orElse(null) ?: throw UserException(UserExceptionCode.USER_NOT_FOUND)

        return UserInfoDto.fromEntity(user)
    }
}