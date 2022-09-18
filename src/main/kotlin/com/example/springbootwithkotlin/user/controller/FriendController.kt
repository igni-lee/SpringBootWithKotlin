package com.example.springbootwithkotlin.user.controller

import com.example.springbootwithkotlin.user.dto.FriendAddDto
import com.example.springbootwithkotlin.user.service.FriendService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/friend")
class FriendController(
    val friendService: FriendService
) {
    @GetMapping("/request/{acceptorId}")
    fun requestList(
        @PathVariable acceptorId: Long,
    ) = friendService.requestList(acceptorId)

    @PostMapping
    fun add(
        @RequestBody
        friendAddDto: FriendAddDto
    ) = friendService.add(friendAddDto)
}
