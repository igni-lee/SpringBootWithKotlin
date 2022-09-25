package com.example.springbootwithkotlin.user.controller

import com.example.springbootwithkotlin.common.response.ResponseData
import com.example.springbootwithkotlin.user.dto.FriendAcceptDto
import com.example.springbootwithkotlin.user.dto.FriendAddDto
import com.example.springbootwithkotlin.user.dto.UnfriendDto
import com.example.springbootwithkotlin.user.service.FriendService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    ) = ResponseData(data = friendService.friendRequestList(acceptorId))

    @PostMapping
    fun add(
        @RequestBody
        friendAddDto: FriendAddDto,
    ) = ResponseData(data = friendService.add(friendAddDto))

    @PutMapping("/request")
    fun accept(
        @RequestBody
        friendAcceptDto: FriendAcceptDto,
    ) = ResponseData(data = friendService.accept(friendAcceptDto))

    @DeleteMapping
    fun unfriend(
        unfriendDto: UnfriendDto,
    ) = ResponseData(data = friendService.unfriend(unfriendDto))
}
