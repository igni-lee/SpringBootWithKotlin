package com.example.springbootwithkotlin.user.repository

import com.example.springbootwithkotlin.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long>