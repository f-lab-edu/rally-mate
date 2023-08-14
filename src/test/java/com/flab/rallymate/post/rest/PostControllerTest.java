package com.flab.rallymate.post.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flab.rallymate.api.PostController;
import com.flab.rallymate.domain.post.PostService;
import com.flab.rallymate.domain.post.dto.PostRequestDTO;
import com.flab.rallymate.error.BaseExceptionHandler;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest {

    private MockMvc client;
    private PostService postService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        postService = mock(PostService.class);
        client = MockMvcBuilders.standaloneSetup(new PostController(postService))
                .setControllerAdvice(new BaseExceptionHandler())
                .build();
    }

    @Test
    void addPost_유효한_구인정보로_구인글_등록요청_시_등록에_성공한다() throws Exception {
        var postRequestDTO = PostRequestDTO.builder()
                .playgroundId(1L)
                .playTime(30)
                .startTime(LocalDateTime.now().plusHours(1L))
                .build();
        String content = objectMapper.writeValueAsString(postRequestDTO);


        client.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Matchers.is("구인글을 등록했어요.")));


        verify(postService).addPost(postRequestDTO);
    }

    @Test
    void addPost_구인글의_시작시간이_현재보다_과거면_BAD_REQUEST를_리턴한다() throws Exception {
        var postRequestDTO = PostRequestDTO.builder()
                .playgroundId(1L)
                .playTime(30)
                .startTime(LocalDateTime.now().minusHours(1L))
                .build();
        String content = objectMapper.writeValueAsString(postRequestDTO);


        client.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("현재 시간 이후로 입력해 주세요.")));
    }

    @Test
    void addPost_구인글_등록시_플레이공간이_누락되었다면_등록에_실패한다() throws Exception {
        var postRequestDTO = PostRequestDTO.builder()
                .playTime(30)
                .startTime(LocalDateTime.now().plusHours(1L))
                .build();
        String content = objectMapper.writeValueAsString(postRequestDTO);


        client.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("플레이 공간을 선택해 주세요.")));
    }

    @Test
    void addPost_구인글_등록시_플레이타임이_누락되었다면_등록에_실패한다() throws Exception {
        var postRequestDTO = PostRequestDTO.builder()
                .playgroundId(1L)
                .startTime(LocalDateTime.now().plusHours(1L))
                .build();
        String content = objectMapper.writeValueAsString(postRequestDTO);


        client.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("플레이 시간을 입력해 주세요.")));
    }

    @Test
    void addPost_구인글_등록시_플레이타임이_음수값이라면_등록에_실패한다() throws Exception {
        var postRequestDTO = PostRequestDTO.builder()
                .playgroundId(1L)
                .startTime(LocalDateTime.now().plusHours(1L))
                .playTime(-1)
                .build();
        String content = objectMapper.writeValueAsString(postRequestDTO);


        client.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("플레이 시간은 0보다 큽니다.")));
    }

    @Test
    void addPost_구인글_등록시_시작시간이_누락되었다면_등록에_실패한다() throws Exception {
        var postRequestDTO = PostRequestDTO.builder()
                .playgroundId(1L)
                .playTime(10)
                .build();
        String content = objectMapper.writeValueAsString(postRequestDTO);


        client.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("시작 시간을 입력해 주세요.")));
    }
}