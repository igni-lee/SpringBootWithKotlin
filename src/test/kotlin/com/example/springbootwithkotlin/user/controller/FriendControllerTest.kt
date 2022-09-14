package com.example.springbootwithkotlin.user.controller

import com.example.springbootwithkotlin.user.dto.FriendAddDto
import com.example.springbootwithkotlin.user.repository.FriendRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("NonAsciiCharacters")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendControllerTest(
    @Autowired
    val friendRepository: FriendRepository,
) {
    @Autowired
    lateinit var mockMvc: MockMvc

    val objectMapper = ObjectMapper()

    @Test
    fun `친구신청을 보낼 수 있다`() {
        mockMvc.post("/friend") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(FriendAddDto(1, 2))
        }.andExpect {
            status { isOk() }
        }.andDo {
            print()
        }

        val friendRelations = friendRepository.findAll()
        friendRelations.forEach {
            println("|${it.id}|${it.requester}|${it.acceptor}|${it.status}|${it.createdAt}|${it.updatedAt}")
        }
    }
}
