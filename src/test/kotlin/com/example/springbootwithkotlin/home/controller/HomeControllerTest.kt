package com.example.springbootwithkotlin.home.controller

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("NonAsciiCharacters")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HomeControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `home주소를 호출하면 허리펴세요 응답이온다`(){
        mockMvc.get("/home")
            .andExpect {
                status { isOk() }
                content {
                    string("허리펴세요")
                }
            }.andDo {
                print()
            }
    }
}