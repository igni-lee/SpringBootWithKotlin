package com.example.springbootwithkotlin.user.dto

data class SmsRequestDto(
    val type: String = "SMS",
    val contentType: String = "COMM",
    val countryCode: String = "82",
    val from: String,
    val content: String = "[IGNI]",
    val messages: List<Message>,
) {
    class Message(
        val to: String,
        val content: String,
    )
}
