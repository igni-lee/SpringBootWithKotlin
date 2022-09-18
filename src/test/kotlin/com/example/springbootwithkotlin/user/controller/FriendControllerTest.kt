package com.example.springbootwithkotlin.user.controller

import com.example.springbootwithkotlin.fixture.Fixture
import com.example.springbootwithkotlin.user.dto.FriendAddDto
import com.example.springbootwithkotlin.user.repository.FriendRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.aspectj.lang.annotation.After
import org.junit.jupiter.api.AfterEach
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

    @BeforeEach
    fun beforeEach() {
        fixture.initData()
    }

    @AfterEach
    fun afterEach() {
        friendRepository.deleteAll()
    }

    @Nested
    inner class `친구신청` {
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

        @Test
        fun `요청 목록을 조회할 수 있다`() {
            mockMvc.get("/friend/request/1")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.length()") { value(4)}
                }.andDo {
                    print()
                }
        }

        @Test
        fun `요청 목록이 없으면 빈 배열을 응답받는다`() {
            mockMvc.get("/friend/request/999999999")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.length()") { value(0)}
                }.andDo {
                    print()
                }
        }
    }

}
