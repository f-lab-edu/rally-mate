package com.flab.rallymate.playground;


import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.member.domain.MemberEntity;
import com.flab.rallymate.domain.playground.PlaygroundService;
import com.flab.rallymate.domain.playground.domain.PlaygroundEntity;
import com.flab.rallymate.domain.playground.domain.PlaygroundRepository;
import com.flab.rallymate.domain.playground.dto.PlaygroundRequestDTO;
import com.flab.rallymate.error.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class PlaygroundEntityServiceTest {

    private PlaygroundService sut;
    private PlaygroundRepository playgroundRepository;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = mock(MemberService.class);
        playgroundRepository = mock(PlaygroundRepository.class);
        sut = new PlaygroundService(memberService, playgroundRepository);
    }

    @Test
    public void addPlace_장소_요청시_등록에_성공한다() {
        String sampleEmail = "sampleEmail";
        var playgroundRequestDTO = PlaygroundRequestDTO.builder()
                .name("sampleName")
                .city("sampleCity")
                .district("sampleDistrict")
                .roadNameAddress("sampleRoadNameAddress")
                .build();
        var findMember = MemberEntity.builder()
                .email(sampleEmail)
                .build();

        when(memberService.getCurrentMemberEmail()).thenReturn(sampleEmail);
        when(memberService.findMemberBy(sampleEmail)).thenReturn(Optional.of(findMember));


        sut.addPlace(playgroundRequestDTO);


        verify(playgroundRepository).save(any(PlaygroundEntity.class));
    }


    @Test
    public void addPlace_존재하지_않는_회원_이메일로_장소등록_시_NOT_FOUND_MEMBER_예외가_발생한다() {

        String sampleEmail = "sampleEmail";
        var playgroundRequestDTO = PlaygroundRequestDTO.builder()
                .name("sampleName")
                .city("sampleCity")
                .district("sampleDistrict")
                .roadNameAddress("sampleRoadNameAddress")
                .build();

        when(memberService.getCurrentMemberEmail()).thenReturn(sampleEmail);
        when(memberService.findMemberBy(sampleEmail)).thenReturn(Optional.empty());


        var baseException = assertThrows(BaseException.class, () -> sut.addPlace(playgroundRequestDTO));


        assertEquals(baseException.getMessage(), NOT_FOUND_MEMBER.getMessage());
    }


}