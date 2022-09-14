package com.example.springbootwithkotlin.user.util

import com.example.springbootwithkotlin.user.dto.SmsRequestDto
import com.example.springbootwithkotlin.user.dto.SmsResponseDto
import com.example.springbootwithkotlin.user.entity.SmsEntity
import com.example.springbootwithkotlin.user.repository.SmsRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class SmsUtil(
    @Value("\${sms.host}")
    val host: String,
    @Value("\${sms.service-id}")
    val serviceId: String,
    @Value("\${sms.path}")
    val path: String,
    @Value("\${sms.type}")
    val type: String,
    @Value("\${sms.access-key}")
    val accessKey: String,
    @Value("\${sms.secret-key}")
    val secretKey: String,
    @Value("\${sms.sender}")
    val sender: String,

    val smsRepository: SmsRepository,
) {
    fun generateSignature(timestamp: String): String {
        val space = " "
        val newLine = "\n"

        val stringBuilder = StringBuilder()
            .append("POST")
            .append(space)
            .append("$path$serviceId$type")
            .append(newLine)
            .append(timestamp)
            .append(newLine)
            .append(accessKey)
            .toString()

        val signingKey = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(signingKey)
        val rawMac = mac.doFinal(stringBuilder.toByteArray(Charsets.UTF_8))

        return Base64.getEncoder().encodeToString(rawMac)
    }

    fun sendSms(to: String) {
        val randomNumber = RandomUtil.generateRandomNumber()
        val message = SmsRequestDto.Message(
            to = to,
            content = "[IGNI-TEST] 인증번호($randomNumber)를 입력해주세요."
        )

        val smsRequestDto = SmsRequestDto(
            from = sender,
            messages = listOf(message)
        )

        val timestamp = System.currentTimeMillis().toString()

        var smsEntity = SmsEntity(
            fromNumber = smsRequestDto.from,
            toNumber = message.to,
            content = message.content,
        )

        val smsResponse = WebClient
            .create("$host$path$serviceId$type")
            .post()
            .headers {
                it.contentType = MediaType.APPLICATION_JSON
                it.add("x-ncp-apigw-timestamp", timestamp)
                it.add("x-ncp-iam-access-key", accessKey)
                it.add("x-ncp-apigw-signature-v2", generateSignature(timestamp))
            }
            .bodyValue(smsRequestDto)
            .retrieve()
            .onStatus(HttpStatus::isError) {
                smsEntity.statusCode = it.statusCode().value()
                smsEntity.statusName = it.statusCode().name

                throw RuntimeException()
            }
            .toEntity(SmsResponseDto::class.java)
            .block()

        val body = smsResponse!!.body!!

        smsEntity.requestId = body.requestId
        smsEntity.requestTime = body.requestTime
        smsEntity.statusCode = smsResponse.statusCodeValue
        smsEntity.statusName = smsResponse.statusCode.name
        smsEntity.authNumber = randomNumber
        smsRepository.save(smsEntity)

        // TODO: WebClient 일반화 작업 필요
    }
}
