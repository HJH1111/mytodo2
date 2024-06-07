package com.example.mytodo2.todoapp

import com.example.mytodo2.comment.model.Comment
import com.example.mytodo2.infra.security.jwt.JwtPlugin
import com.example.mytodo2.todoapp.controller.dto.TodoResponse
import com.example.mytodo2.todoapp.service.TodoService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import java.time.LocalDateTime


@SpringBootTest
@AutoConfigureMockMvc // mockMvc 주입용도
@ExtendWith(MockKExtension::class) // mockk쓸 때 표기 해줘야 함
class TodoControllerTest @Autowired constructor(
    private val mockMvc: MockMvc, private val jwtPlugin: JwtPlugin,
) : DescribeSpec({
    extension(SpringExtension)

    // describe 끝날시 마다 설정한 mocking 비워줌
    afterContainer {
        clearAllMocks()
    }

    // CourseService Mocking
    val todoService = mockk<TodoService>()

    describe("GET /courses/{id}는") {
        context("존재하는 ID를 요청을 보낼 때") {
            it("200 status code를 응답해야한다.") {
                val todoId = 1L

                // Mock의 동작 정의
                every { todoService.getTodo(any()) } returns TodoResponse(
                    id = todoId,
                    title = "test_title",
                    content = "abc",
                    writer = "aaa",
                    date = LocalDateTime.now(),

                )

                // AccessToken 생성
                val jwtToken = jwtPlugin.generateAccessToken(
                    subject = "1",
                    email = "test@gmail.com",
                    role = "DOMAIN"
                )

                // 요청 수행
                val result = mockMvc.perform(
                    get("/todos/$todoId")
                        .header("Authorization", "Bearer $jwtToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andReturn()

                // status code 확인
                result.response.status shouldBe 200

                // json string으로 부터 CourseResponse 생성
                val responseDto = jacksonObjectMapper().readValue(
                    result.response.contentAsString,
                    TodoResponse::class.java
                )
                // id 동일한지 확인
                responseDto.id shouldBe todoId
            }
        }
    }
})