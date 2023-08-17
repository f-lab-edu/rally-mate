package com.flab.rallymate.rallyplace;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.rallymate.rallyplace.controller.RallyPlaceController;
import com.flab.rallymate.rallyplace.RallyPlaceService;
import com.flab.rallymate.rallyplace.domain.RallyPlaceRequestDTO;
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


public class RallyPlaceEntityControllerTest {
    private MockMvc client;
    private RallyPlaceService rallyPlaceService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        rallyPlaceService = mock(RallyPlaceService.class);
        client = MockMvcBuilders.standaloneSetup(new RallyPlaceController(rallyPlaceService))
                .setControllerAdvice(new BaseExceptionHandler())
                .build();
    }

    @Test
    void addRallyPlace_유효한_장소정보로_장소등록시_성공한다() throws Exception {
        var rallyPlaceRequestDTO = RallyPlaceRequestDTO.builder()
                .name("sampleName")
                .city("sampleCity")
                .district("sampleDistrict")
                .roadNameAddress("sampleRoadNameAddress")
                .build();
        String content = objectMapper.writeValueAsString(rallyPlaceRequestDTO);


        client.perform(post("/api/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Matchers.is("장소를 등록했어요!")));


        verify(rallyPlaceService).addRallyPlace(rallyPlaceRequestDTO);

    }


    @Test
    void addRallyPlace_존재하지_않는_유저가_장소등록_요청_시에는_401Unauthorized_에러를_반환한다() throws Exception {

        var rallyPlaceRequestDTO = RallyPlaceRequestDTO.builder()
                .name("sampleName")
                .city("sampleCity")
                .district("sampleDistrict")
                .roadNameAddress("sampleRoadNameAddress")
                .build();

        String content = objectMapper.writeValueAsString(rallyPlaceRequestDTO);
        doThrow(new BaseException(NOT_FOUND_MEMBER))
                .when(rallyPlaceService).addRallyPlace(rallyPlaceRequestDTO);


        client.perform(post("/api/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isUnauthorized());


        verify(rallyPlaceService).addRallyPlace(rallyPlaceRequestDTO);
    }

}