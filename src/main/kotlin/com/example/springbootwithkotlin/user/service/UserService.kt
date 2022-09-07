package com.example.springbootwithkotlin.user.service

import com.example.springbootwithkotlin.user.dto.SignupDto
import com.example.springbootwithkotlin.user.entity.UserEntity
import com.example.springbootwithkotlin.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
) {
    fun signup(signupDto: SignupDto) {
        val newUser = UserEntity.from(signupDto)
        userRepository.save(newUser)
    }
}