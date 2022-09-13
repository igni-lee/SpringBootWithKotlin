package com.example.springbootwithkotlin.user.controller

import com.example.springbootwithkotlin.fixture.Fixture
import com.example.springbootwithkotlin.user.dto.LoginDto
import com.example.springbootwithkotlin.user.dto.SignupDto
import com.example.springbootwithkotlin.user.repository.UserRepository
import com.example.springbootwithkotlin.user.util.CryptUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
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
class UserControllerTest(
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val fixture: Fixture,
) {
    @Autowired
    lateinit var mockMvc: MockMvc

    val objectMapper = ObjectMapper()
    val basicUser = Fixture.User.basicUser

    @BeforeEach
    fun beforeEach() {
        fixture.initData()
    }

    @AfterEach
    fun afterEach() {
        userRepository.deleteAll()
    }

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
            jsonPath("$.code") { value("invalid_name")}
            jsonPath("$.message") { value("이름은 두글자 이상이어야 합니다.")}
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
            jsonPath("$.code") { value("invalid_name")}
            jsonPath("$.message") { value("이름에 들어갈 수 없는 문자가 있습니다.")}
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
            jsonPath("$.code") { value("invalid_email")}
            jsonPath("$.message") { value("이메일은 공백일 수 없습니다.")}
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
            jsonPath("$.code") { value("invalid_email")}
            jsonPath("$.message") { value("올바른 이메일 형식이 아닙니다.")}
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
            jsonPath("$.code") { value("invalid_email")}
            jsonPath("$.message") { value("유효하지 않은 도메인입니다.")}
        }.andDo {
            print()
        }
    }

    @Test
    fun `중복된 이메일로 가입할 수 없다`() {
        val signupDto = SignupDto(
            name = "이그니",
            email = "test@test.com",
            phoneNumber = "01083860731",
            password = "password",
        )

        mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(signupDto)
        }.andExpect {
            status { isConflict() }
            jsonPath("$.code") { value("email_duplicate")}
            jsonPath("$.message") { value("중복된 이메일입니다.")}
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
            jsonPath("$.code") { value("invalid_password")}
            jsonPath("$.message") { value("비밀번호는 6자 이상, 12자 이하여야합니다.")}
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
            jsonPath("$.code") { value("invalid_password")}
            jsonPath("$.message") { value("비밀번호는 6자 이상, 12자 이하여야합니다.")}
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
            jsonPath("$.code") { value("invalid_password")}
            jsonPath("$.message") { value("비밀번호에 포함할 수 없는 특수문자가 있습니다.")}
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
            jsonPath("$.code") { value("invalid_phone_number")}
            jsonPath("$.message") { value("휴대폰 번호는 공백일 수 없습니다.")}
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
            jsonPath("$.code") { value("invalid_phone_number")}
            jsonPath("$.message") { value("휴대폰 번호는 숫자만 입력해주세요.")}
        }.andDo {
            print()
        }
    }

    @Test
    fun `이메일과 비밀번호로 로그인할 수 있다`(){
        val loginDto = LoginDto(
            email = "test@test.com",
            password = "password",
        )

        mockMvc.post("/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginDto)
        }.andExpect {
            status { isOk() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `이메일이 공백이면 로그인할 수 없다`(){
        val loginDto = LoginDto(
            email = "     ",
            password = "password",
        )

        mockMvc.post("/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginDto)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("invalid_email")}
            jsonPath("$.message") { value("이메일은 공백일 수 없습니다.")}
        }.andDo {
            print()
        }
    }

    @Test
    fun `존재하지 않는 이메일로 로그인할 수 없다`(){
        val loginDto = LoginDto(
            email = "test@asdfasdf.com",
            password = "password",
        )

        mockMvc.post("/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginDto)
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.code") { value("invalid_login")}
            jsonPath("$.message") { value("로그인 정보를 재확인해주세요.")}
        }.andDo {
            print()
        }
    }

    @Test
    fun `이메일 형식이 유효하지 않으면 로그인할 수 없다`(){
        val loginDto = LoginDto(
            email = "test@test@com",
            password = "password",
        )

        mockMvc.post("/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginDto)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("invalid_email")}
            jsonPath("$.message") { value("올바른 이메일 형식이 아닙니다.")}
        }.andDo {
            print()
        }
    }

    @Test
    fun `비밀번호가 다르면 로그인할 수 없다`(){
        val loginDto = LoginDto(
            email = "test@test.com",
            password = "fakeword",
        )

        mockMvc.post("/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginDto)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("invalid_login")}
            jsonPath("$.message") { value("로그인 정보를 재확인해주세요.")}
        }.andDo {
            print()
        }
    }

    @Test
    fun `비밀번호가 여섯글자 미만이면 로그인할 수 없다`(){
        val loginDto = LoginDto(
            email = "test@test.com",
            password = "pwd",
        )

        mockMvc.post("/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginDto)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("invalid_password")}
            jsonPath("$.message") { value("비밀번호는 6자 이상, 12자 이하여야합니다.")}
        }.andDo {
            print()
        }
    }

    @Test
    fun `비밀번호가 열두글자 초과면 로그인 할 수 없다`(){
        val loginDto = LoginDto(
            email = "test@test.com",
            password = "passwordpasswordpassword",
        )

        mockMvc.post("/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginDto)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("invalid_password")}
            jsonPath("$.message") { value("비밀번호는 6자 이상, 12자 이하여야합니다.")}
        }.andDo {
            print()
        }
    }

    @Test
    fun `비밀번호에 키보드로 입력할 수 없는 문자가 포함되면 로그인할 수 없다`(){
        val loginDto = LoginDto(
            email = "test@test.com",
            password = "p☠ssword",
        )

        mockMvc.post("/user/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginDto)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("invalid_password")}
            jsonPath("$.message") { value("비밀번호에 포함할 수 없는 특수문자가 있습니다.")}
        }.andDo {
            print()
        }
    }

    @Test
    fun `이메일로 사용자를 검색할 수 있다`(){
        mockMvc.get("/user/${basicUser.email}")
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(basicUser.id)}
                jsonPath("$.name") { value(basicUser.name)}
                jsonPath("$.email") { value(basicUser.email)}
                jsonPath("$.phone_number") { value(basicUser.phoneNumber)}
            }.andDo {
                print()
            }
    }

    @Test
    fun `존재하지 않는 이메일로 사용자를 검색할 수 없다`() {
        mockMvc.get("/user/notexist@test.com")
            .andExpect {
                status { isNotFound() }
            }.andDo {
                print()
            }
    }
}