package com.flab.rallymate.playground.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.rallymate.api.PlaygroundController;
import com.flab.rallymate.domain.playground.PlaygroundService;
import com.flab.rallymate.domain.playground.dto.PlaygroundRequestDTO;
import com.flab.rallymate.error.BaseException;
import com.flab.rallymate.error.BaseExceptionHandler;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_MEMBER;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlaygroundControllerTest {

    private MockMvc client;
    private PlaygroundService playgroundService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        playgroundService = mock(PlaygroundService.class);
        client = MockMvcBuilders.standaloneSetup(new PlaygroundController(playgroundService))
                .setControllerAdvice(new BaseExceptionHandler())
                .build();
    }

    @Test
    void addPlace_유효한_장소정보로_장소등록시_성공한다() throws Exception {
        var playgroundRequestDTO = PlaygroundRequestDTO.builder()
                .name("sampleName")
                .city("sampleCity")
                .district("sampleDistrict")
                .roadNameAddress("sampleRoadNameAddress")
                .build();
        String content = objectMapper.writeValueAsString(playgroundRequestDTO);


        client.perform(post("/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Matchers.is("장소를 등록했어요!")));

        verify(playgroundService).addPlace(playgroundRequestDTO);
    }

    @Test
    void addPlace_존재하지_않는_유저가_장소등록_요청_시에는_401Unauthorized_에러를_반환한다() throws Exception {
        var playgroundRequestDTO = PlaygroundRequestDTO.builder()
                .name("sampleName")
                .city("sampleCity")
                .district("sampleDistrict")
                .roadNameAddress("sampleRoadNameAddress")
                .build();
        String content = objectMapper.writeValueAsString(playgroundRequestDTO);
        doThrow(new BaseException(NOT_FOUND_MEMBER))
                .when(playgroundService).addPlace(playgroundRequestDTO);


        client.perform(post("/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isUnauthorized());

        verify(playgroundService).addPlace(playgroundRequestDTO);
    }

}