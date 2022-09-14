package com.example.springbootwithkotlin.user.dto

import com.example.springbootwithkotlin.user.entity.UserEntity

data class UserInfoDto(
    val id: Long,
    val name: String,
    val email: String,
    val phoneNumber: String,
) {
    companion object {
        fun fromEntity(userEntity: UserEntity) =
            UserInfoDto(
                id = userEntity.id!!,
                name = userEntity.name,
                email = userEntity.email,
                phoneNumber = userEntity.phoneNumber,
            )
    }
}
