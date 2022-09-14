package com.example.springbootwithkotlin.user.util

import com.example.springbootwithkotlin.user.repository.SmsRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Suppress("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SmsUtilTest(
    @Autowired
    val smsUtil: SmsUtil,
    @Autowired
    val smsRepository: SmsRepository,
) {

    @Test
    fun `SMS로 인증번호를 보낼 수 있다`() {
        smsUtil.sendSms("01083860731")
        smsRepository.findAll().map {
            println("${it.id} | from : ${it.fromNumber} | to : ${it.toNumber} | ${it.content} | ${it.statusCode} | ${it.statusName}")
        }
    }
}
