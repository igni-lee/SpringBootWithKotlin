package com.example.springbootwithkotlin.user.controller

import com.example.springbootwithkotlin.user.dto.FriendAddDto
import com.example.springbootwithkotlin.user.service.FriendService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/friend")
class FriendController(
    val friendService: FriendService
) {

    @PostMapping
    fun add(
        @RequestBody
        friendAddDto: FriendAddDto
    ) = friendService.add(friendAddDto)
}
