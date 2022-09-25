package com.example.springbootwithkotlin.user.entity

import com.example.springbootwithkotlin.common.entity.BaseEntity
import com.example.springbootwithkotlin.user.dto.SignupDto
import com.example.springbootwithkotlin.user.util.CryptUtil
import com.example.springbootwithkotlin.user.util.RandomUtil
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(name = "uk_user_email", columnNames = ["email"])])
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom")
    @GenericGenerator(name = "custom", strategy = "com.example.springbootwithkotlin.common.entity.IdOrGenerate")
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "email", nullable = false)
    var email: String,

    @Column(name = "phone_number", nullable = false)
    var phoneNumber: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "password_salt", nullable = false)
    var passwordSalt: String,
) : BaseEntity() {
    companion object {
        fun from(signupDto: SignupDto): UserEntity {
            val passwordSalt = RandomUtil.generateSalt()
            return UserEntity(
                name = signupDto.name,
                email = signupDto.email,
                phoneNumber = signupDto.phoneNumber,
                password = CryptUtil.crypt(signupDto.password, passwordSalt),
                passwordSalt = passwordSalt,
            )
        }
    }
}
