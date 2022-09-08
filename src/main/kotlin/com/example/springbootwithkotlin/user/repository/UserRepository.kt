package com.example.springbootwithkotlin.user.repository

import com.example.springbootwithkotlin.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String) : Optional<UserEntity>
}