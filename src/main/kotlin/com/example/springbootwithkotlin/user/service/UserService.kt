package com.example.springbootwithkotlin.user.service

import com.example.springbootwithkotlin.user.constant.UserResponseCode
import com.example.springbootwithkotlin.user.dto.LoginDto
import com.example.springbootwithkotlin.user.dto.SignupDto
import com.example.springbootwithkotlin.user.dto.UserInfoDto
import com.example.springbootwithkotlin.user.entity.UserEntity
import com.example.springbootwithkotlin.user.exception.UserException
import com.example.springbootwithkotlin.user.repository.UserRepository
import com.example.springbootwithkotlin.user.repository.UserRepositorySupport
import com.example.springbootwithkotlin.user.util.CryptUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    val userRepository: UserRepository,
    val userRepositorySupport: UserRepositorySupport,
) {
    @Transactional
    fun signup(signupDto: SignupDto) {
        userRepository.findByEmail(signupDto.email).ifPresent {
            throw UserException(UserResponseCode.EMAIL_DUPLICATE)
        }

        userRepository.save(UserEntity.from(signupDto))
    }

    @Transactional
    fun login(loginDto: LoginDto) {
        val user = userRepositorySupport.findByEmail(loginDto.email) ?: throw UserException(UserResponseCode.LOGIN_FAIL)

        if (user.password != CryptUtil.crypt(loginDto.password, user.passwordSalt)) {
            throw UserException(UserResponseCode.LOGIN_FAIL)
        }
    }

    @Transactional
    fun searchByEmail(email: String): UserInfoDto {
        val user = userRepositorySupport.findByEmail(email) ?: throw UserException(UserResponseCode.USER_NOT_FOUND)

        return UserInfoDto.fromEntity(user)
    }
}
