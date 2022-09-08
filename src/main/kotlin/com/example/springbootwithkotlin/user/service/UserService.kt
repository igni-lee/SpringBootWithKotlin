package com.example.springbootwithkotlin.user.service

import com.example.springbootwithkotlin.user.dto.SignupDto
import com.example.springbootwithkotlin.user.entity.UserEntity
import com.example.springbootwithkotlin.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    val userRepository: UserRepository,
) {
    @Transactional
    fun signup(signupDto: SignupDto) = userRepository.save(UserEntity.from(signupDto))
}