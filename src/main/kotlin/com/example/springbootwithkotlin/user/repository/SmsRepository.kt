package com.example.springbootwithkotlin.user.repository

import com.example.springbootwithkotlin.user.entity.SmsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SmsRepository : JpaRepository<SmsEntity, Long>
