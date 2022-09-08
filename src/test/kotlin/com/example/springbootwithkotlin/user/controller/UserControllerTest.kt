package com.example.springbootwithkotlin.user.controller

import com.example.springbootwithkotlin.user.dto.SignupDto
import com.example.springbootwithkotlin.user.repository.UserRepository
import com.example.springbootwithkotlin.user.util.CryptUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
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
class UserControllerTest(
    @Autowired
    val userRepository: UserRepository,
) {
    @Autowired
    lateinit var mockMvc: MockMvc

    val objectMapper = ObjectMapper()

    @Test
    fun `올바른 이메일과 비밀번호로 가입할 수 있다`() {
        val signupDto = SignupDto(
            name = "이그니",
            email = "igni@igni.com",
            phoneNumber = "01083860731",
            password = "password",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isOk() }
        }.andDo {
            print()
        }

        val signupUser = userRepository.findByEmail(signupDto.email).get()
        println(signupUser)
        Assertions.assertNotNull(signupUser.id)
        Assertions.assertEquals(signupUser.password, CryptUtil.crypt(signupDto.password, signupUser.passwordSalt))
        Assertions.assertNotNull(signupUser.createdAt)
        Assertions.assertNotNull(signupUser.updatedAt)
    }

    @Test
    fun `이름이 한글자면 가입할 수 없다`(){
        val signupDto = SignupDto(
            name = "이",
            email = "igni@igni.com",
            phoneNumber = "01083860731",
            password = "password",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `이름에 알파벳,한글 외 문자가 있으면 가입할 수 없다`(){
        val signupDto = SignupDto(
            name = "이☠︎니",
            email = "igni@igni.com",
            phoneNumber = "01083860731",
            password = "password",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `이메일은 공백일 수 없다`(){
        val signupDto = SignupDto(
            name = "이그니",
            email = "           ",
            phoneNumber = "01083860731",
            password = "password",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `올바른 이메일 형식이 아니면 가입할 수 없다`(){
        val signupDto = SignupDto(
            name = "이그니",
            email = "igni@asdfasdfasdfasdfasdfwe@.qwer",
            phoneNumber = "01083860731",
            password = "password",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `유효하지 않은 도메인으로 가입할 수 없다`(){
        val signupDto = SignupDto(
            name = "이그니",
            email = "igni@qlwkerjliasdjfliogjjksdhagklasd.com",
            phoneNumber = "01083860731",
            password = "password",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `비밀번호가 여섯글자 미만이면 가입할 수 없다`(){
        val signupDto = SignupDto(
            name = "이그니",
            email = "igni@igni.com",
            phoneNumber = "01083860731",
            password = "pass",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `비밀번호가 열두글자를 초과하면 가입할 수 없다`(){
        val signupDto = SignupDto(
            name = "이그니",
            email = "igni@igni.com",
            phoneNumber = "01083860731",
            password = "passwordpasswordpasswordpassword",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `비밀번호에 키보드로 입력할 수 없는 특수문자가 있으면 가입할 수 없다`(){
        val signupDto = SignupDto(
            name = "이그니",
            email = "igni@igni.com",
            phoneNumber = "01083860731",
            password = "password☠︎",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `휴대폰 번호가 공백이면 가입할 수 없다`(){
        val signupDto = SignupDto(
            name = "이그니",
            email = "igni@igni.com",
            phoneNumber = "           ",
            password = "password",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `휴대폰 번호에 숫자외 문자가 있으면 가입할 수 없다`(){
        val signupDto = SignupDto(
            name = "이그니",
            email = "igni@igni.com",
            phoneNumber = "010-8386-0731",
            password = "password",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            print()
        }
    }
}