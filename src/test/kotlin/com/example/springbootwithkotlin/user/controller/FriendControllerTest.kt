package com.example.springbootwithkotlin.user.controller

import com.example.springbootwithkotlin.fixture.Fixture
import com.example.springbootwithkotlin.user.constant.FriendAddStatus
import com.example.springbootwithkotlin.user.constant.UserResponseCode
import com.example.springbootwithkotlin.user.dto.FriendAcceptDto
import com.example.springbootwithkotlin.user.dto.FriendAddDto
import com.example.springbootwithkotlin.user.repository.FriendRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("NonAsciiCharacters")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendControllerTest(
    @Autowired
    val fixture: Fixture,
    @Autowired
    val friendRepository: FriendRepository,
) {
    @Autowired
    lateinit var mockMvc: MockMvc

    val objectMapper = ObjectMapper()

    val friendRequest2 = Fixture.Friend.friendRequest2

    @Nested
    @Suppress("NonAsciiCharacters")
    inner class 친구신청 {
        @BeforeEach
        fun beforeEach() = fixture.initData()

        @Test
        fun `친구신청을 보낼 수 있다`() {
            mockMvc.post("/friend") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(FriendAddDto(1, 2))
            }.andExpect {
                status { isOk() }
                jsonPath("$.code") { value(UserResponseCode.SUCCESS.name) }
                jsonPath("$.data") { isEmpty() }
            }.andDo {
                print()
            }

            val friendRelations = friendRepository.findAll()
            friendRelations.forEach {
                println("|${it.id}|${it.requester}|${it.acceptor}|${it.status}|${it.createdAt}|${it.updatedAt}")
            }
        }

        @Test
        fun `요청 목록을 조회할 수 있다`() {
            mockMvc.get("/friend/request/1")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.code") { value(UserResponseCode.SUCCESS.name) }
                    jsonPath("$.data") { isNotEmpty() }
                    jsonPath("$.data.length()") { value(4) }
                }.andDo {
                    print()
                }
        }

        @Test
        fun `요청 목록이 없으면 빈 배열을 응답받는다`() {
            mockMvc.get("/friend/request/999999999")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.code") { value(UserResponseCode.SUCCESS.name) }
                    jsonPath("$.data") { isEmpty() }
                    jsonPath("$.data.length()") { value(0) }
                }.andDo {
                    print()
                }
        }

        @Test
        fun `친구신청을 수락할 수 있다`() {
            val friendRelations = friendRepository.findAll()
            friendRelations.forEach {
                println("|${it.id}|${it.requester}|${it.acceptor}|${it.status}|${it.createdAt}|${it.updatedAt}")
            }

            val friendAcceptDto = FriendAcceptDto(
                requestId = friendRequest2.id!!,
                acceptor = friendRequest2.acceptor,
                requester = friendRequest2.requester,
            )

            mockMvc.put("/friend/request") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(friendAcceptDto)
            }.andExpect {
                status { isOk() }
                jsonPath("$.code") { value(UserResponseCode.SUCCESS.name) }
                jsonPath("$.data") { isEmpty() }
            }.andDo {
                print()
            }

            val acceptedFriendRequest = friendRepository.findById(friendRequest2.id!!).get()
            Assertions.assertEquals(FriendAddStatus.ACCEPTED, acceptedFriendRequest.status)
            Assertions.assertNotEquals(friendRequest2.updatedAt, acceptedFriendRequest.updatedAt)
        }

        @Test
        fun `유효하지 않은 친구 신청은 204응답을 받는다`() {
            val friendAcceptDto = FriendAcceptDto(
                requestId = 98127397816,
                acceptor = friendRequest2.acceptor,
                requester = friendRequest2.requester,
            )

            mockMvc.put("/friend/request") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(friendAcceptDto)
            }.andExpect {
                status { isNoContent() }
                jsonPath("$.code") { value(UserResponseCode.NOT_EXIST_FRIEND_REQUEST.name) }
                jsonPath("$.message") { value("존재하지 않는 친구요청입니다.") }
            }.andDo {
                print()
            }

            val acceptedFriendRequest = friendRepository.findById(friendRequest2.id!!).get()
            Assertions.assertEquals(FriendAddStatus.PENDING, acceptedFriendRequest.status)
        }
    }
}
