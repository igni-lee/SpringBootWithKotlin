package com.example.springbootwithkotlin.home.controller

import com.example.springbootwithkotlin.home.controller.dto.SnakeToCamelDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/home")
class HomeController {

    @GetMapping("")
    fun home() = "허리펴세요"
    @GetMapping("/uri/snake_to_camel")
    fun uriSnakeToCamel(snakeToCamelDto: SnakeToCamelDto) = snakeToCamelDto

    @PostMapping("/urlencoded/snake_to_camel")
    fun urlEncodedSnakeToCamel(snakeToCamelDto: SnakeToCamelDto) = snakeToCamelDto

    @PostMapping("/json/snake_to_camel")
    fun jsonSnakeToCamel(@RequestBody snakeToCamelDto: SnakeToCamelDto): SnakeToCamelDto {
        println("########### $snakeToCamelDto")
        val newDto = SnakeToCamelDto(
            token = snakeToCamelDto.token,
            accessToken = snakeToCamelDto.accessToken,
            refreshToken = snakeToCamelDto.refreshToken,
        )
        return newDto
    }
}