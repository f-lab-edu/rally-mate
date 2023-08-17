package com.flab.rallymate.rallyplace;


import com.flab.rallymate.member.MemberService;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.rallyplace.domain.RallyPlaceEntity;
import com.flab.rallymate.rallyplace.repository.RallyPlaceRepository;
import com.flab.rallymate.rallyplace.domain.RallyPlaceRequestDTO;
import com.flab.rallymate.error.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class RallyPlaceEntityServiceTest {

    private RallyPlaceService sut;
    private RallyPlaceRepository rallyPlaceRepository;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = mock(MemberService.class);
        rallyPlaceRepository = mock(RallyPlaceRepository.class);
        sut = new RallyPlaceService(memberService, rallyPlaceRepository);
    }

    @Test
    public void addRallyPlace_장소_요청시_등록에_성공한다() {
        String sampleEmail = "sampleEmail";
        var rallyPlaceRequestDTO = RallyPlaceRequestDTO.builder()
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


        sut.addRallyPlace(rallyPlaceRequestDTO);


        verify(rallyPlaceRepository).save(any(RallyPlaceEntity.class));
    }


    @Test
    public void addRallyPlace_존재하지_않는_회원_이메일로_장소등록_시_NOT_FOUND_MEMBER_예외가_발생한다() {

        String sampleEmail = "sampleEmail";
        var rallyPlaceRequestDTO = RallyPlaceRequestDTO.builder()
                .name("sampleName")
                .city("sampleCity")
                .district("sampleDistrict")
                .roadNameAddress("sampleRoadNameAddress")
                .build();

        when(memberService.getCurrentMemberEmail()).thenReturn(sampleEmail);
        when(memberService.findMemberBy(sampleEmail)).thenReturn(Optional.empty());


        var baseException = assertThrows(BaseException.class, () -> sut.addRallyPlace(rallyPlaceRequestDTO));


        assertEquals(baseException.getMessage(), NOT_FOUND_MEMBER.getMessage());
    }


}