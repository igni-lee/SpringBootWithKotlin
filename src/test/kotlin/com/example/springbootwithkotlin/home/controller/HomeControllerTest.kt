package com.example.springbootwithkotlin.home.controller

import mu.KLogging
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
class HomeControllerTest {
    companion object : KLogging()

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `home주소를 호출하면 허리펴세요 응답이온다`() {
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

    @Test
    fun `로그를 콘솔,파일로 남긴다`() {
        logger.debug("##### debug level log")
        logger.info("##### info level log")
        logger.warn("##### warn level log")
        logger.error("##### error level log")
    }

    @Test
    fun `URI에 SnakeCase로 보내도 CamelCase로 변경된다`() {
        mockMvc.get("/home/uri/snake_to_camel?token=fake_token&access_token=fake_access_token&refresh_token=fake_refresh_token")
            .andExpect {
                status { isOk() }
                jsonPath("$.token") { value("fake_token") }
                jsonPath("$.access_token") { value("fake_access_token") }
                jsonPath("$.refresh_token") { value("fake_refresh_token") }
            }.andDo {
                print()
            }
    }

    @Test
    fun `MediaType이 x-www-form-urlencoded 일때 SnakeCase로 보내도 CamelCase로 변경된다`() {
        mockMvc.post("/home/urlencoded/snake_to_camel") {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("token", "fake_token")
            param("access_token", "fake_access_token")
            param("refresh_token", "fake_refresh_token")
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { value("fake_token") }
            jsonPath("$.access_token") { value("fake_access_token") }
            jsonPath("$.refresh_token") { value("fake_refresh_token") }
        }.andDo {
            print()
        }
    }

    @Test
    fun `MediaType이 json 일때 SnakeCase로 보내도 CamelCase로 변경된다`() {
        mockMvc.post("/home/json/snake_to_camel") {
            contentType = MediaType.APPLICATION_JSON
            content = """{
                "token": "fake_token",
                "access_token":"fake_access_token",
                "refresh_token":"fake_refresh_token"
            }"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { value("fake_token") }
            jsonPath("$.access_token") { value("fake_access_token") }
            jsonPath("$.refresh_token") { value("fake_refresh_token") }
        }.andDo {
            print()
        }
    }
}