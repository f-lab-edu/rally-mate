package com.flab.rallymate.rallyschedule;


import com.flab.rallymate.global.Address;
import com.flab.rallymate.member.MemberService;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.rallyplace.RallyPlaceService;
import com.flab.rallymate.rallyplace.domain.RallyPlaceEntity;
import com.flab.rallymate.rallyschedule.domain.RallyScheduleEntity;
import com.flab.rallymate.rallyschedule.domain.RallyScheduleSpecification;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleResponseDTO;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleSearchDTO;
import com.flab.rallymate.rallyschedule.repository.RallyScheduleRepository;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleRequestDTO;
import com.flab.rallymate.error.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_MEMBER;
import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_PLAYGROUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class RallyScheduleServiceTest {

    private RallyScheduleService sut;
    private RallyScheduleRepository rallyScheduleRepository;
    private MemberService memberService;
    private RallyPlaceService rallyPlaceService;

    @BeforeEach
    void setUp() {
        rallyPlaceService = mock(RallyPlaceService.class);
        memberService = mock(MemberService.class);
        rallyScheduleRepository = mock(RallyScheduleRepository.class);
        sut = new RallyScheduleService(rallyScheduleRepository, memberService, rallyPlaceService);
    }

    @Test
    void addRallySchedule_메이트_구인글_등록요청시_등록에_성공한다() throws Exception {

        var rallyScheduleRequestDTO = RallyScheduleRequestDTO.builder()
                .playgroundId(1L)
                .playTime(120)
                .startTime(LocalDateTime.of(2023, 7, 28, 13, 30))
                .build();

        String email = "sample@sample.com";
        var findMember = MemberEntity.builder().id(2L).name("nathan").build();
        var findPlayground = RallyPlaceEntity.builder().id(rallyScheduleRequestDTO.playgroundId()).name("송실내테니스").build();
        var rallySchedule = rallyScheduleRequestDTO.toRallyScheduleEntity(findMember, findPlayground);

        when(memberService.getCurrentMemberEmail()).thenReturn(email);
        when(memberService.findMemberBy(email)).thenReturn(Optional.ofNullable(findMember));
        when(rallyPlaceService.findRallyPlaceBy(rallyScheduleRequestDTO.playgroundId())).thenReturn(Optional.ofNullable(findPlayground));


        sut.addRallySchedule(rallyScheduleRequestDTO);


        verify(rallyScheduleRepository).save(rallySchedule);
        assertEquals(rallyScheduleRequestDTO.playTime(), rallySchedule.getPlayTime());
        assertEquals(findMember, rallySchedule.getMember());
        assertEquals(findPlayground, rallySchedule.getRallyPlace());
        assertEquals(rallyScheduleRequestDTO.startTime(), rallySchedule.getStartTime());
    }


    @Test
    void addRallySchedule_구인_글_작성_요청시_조회된_회원정보가_없다면_NOT_FOUND_MEMBER_예외가_발생한다() throws Exception {

        var rallyScheduleRequestDTO = RallyScheduleRequestDTO.builder()
                .playgroundId(1L)
                .playTime(120)
                .startTime(LocalDateTime.of(2023, 7, 28, 13, 30, 0))
                .build();
        String email = "sample@sample.com";
        var findPlayground = RallyPlaceEntity.builder().id(rallyScheduleRequestDTO.playgroundId()).name("송실내테니스").build();

        when(memberService.getCurrentMemberEmail()).thenReturn(email);
        when(memberService.findMemberBy(email)).thenReturn(Optional.empty());
        when(rallyPlaceService.findRallyPlaceBy(rallyScheduleRequestDTO.playgroundId())).thenReturn(Optional.ofNullable(findPlayground));


        var baseException = assertThrows(BaseException.class, () -> sut.addRallySchedule(rallyScheduleRequestDTO));


        assertEquals(baseException.getMessage(), NOT_FOUND_MEMBER.getMessage());
    }

    @Test
    void addRallySchedule_구인_글_작성_요청시_조회된_플레이장소가_없다면_NOT_FOUND_PLAYGROUND_예외가_발생한다() {

        var rallyScheduleRequestDTO = RallyScheduleRequestDTO.builder()
                .playgroundId(1L)
                .playTime(120)
                .startTime(LocalDateTime.of(2023, 7, 28, 13, 30, 0))
                .build();
        String email = "sample@sample.com";
        var findMember = MemberEntity.builder().id(2L).name("nathan").build();

        when(memberService.getCurrentMemberEmail()).thenReturn(email);
        when(memberService.findMemberBy(email)).thenReturn(Optional.ofNullable(findMember));
        when(rallyPlaceService.findRallyPlaceBy(rallyScheduleRequestDTO.playgroundId())).thenReturn(Optional.empty());


        var baseException = assertThrows(BaseException.class, () -> sut.addRallySchedule(rallyScheduleRequestDTO));


        assertEquals(baseException.getMessage(), NOT_FOUND_PLAYGROUND.getMessage());
    }

    @Test
    void getRallySchedules_구인글_목록조회_요청시_성공한다() {

        RallyScheduleEntity rallySchedule1 = RallyScheduleEntity.builder()
                .id(1L)
                .playTime(120)
                .startTime(LocalDateTime.of(2023, 7, 28, 13, 30, 0))
                .member(MemberEntity.builder().id(1L).name("nathan").build())
                .rallyPlace(RallyPlaceEntity.builder().id(1L)
                        .address(
                                Address.builder().city("서울시").district("강남구").roadNameAddress("서울시 강남구 12-1").build()
                        ).name("송실내테니스").build()
                )
                .build();
        RallyScheduleEntity rallySchedule2 = RallyScheduleEntity.builder()
                .id(2L)
                .playTime(60)
                .startTime(LocalDateTime.of(2023, 8, 21, 13, 30, 0))
                .member(MemberEntity.builder().id(2L).name("hjun").build())
                .rallyPlace(RallyPlaceEntity.builder().id(1L)
                        .address(
                                Address.builder().city("서울시").district("마포구").roadNameAddress("서울시 마포구 구룡길 11-1").build()
                        ).name("상암 실내테니스").build()
                )
                .build();
        var rallyScheduleEntities = List.of(rallySchedule1, rallySchedule2);
        when(rallyScheduleRepository.findAll()).thenReturn(rallyScheduleEntities);


        var rallyScheduleResponseDTOS = sut.getRallySchedules(RallyScheduleSearchDTO.builder().build());


        verify(rallyScheduleRepository).findAll();
        assertEquals(rallyScheduleEntities.size(), rallyScheduleResponseDTOS.size());
        assertEquals(rallyScheduleEntities.get(0).getId(), rallyScheduleResponseDTOS.get(0).id());

    }

    @Test
    void getRallySchedule_특정_검색조건에_해당하는_구인글_목록조회시_조회에_성공한다() {
		RallyScheduleEntity rallySchedule1 = RallyScheduleEntity.builder()
			.id(1L)
			.playTime(120)
			.startTime(LocalDateTime.of(2023, 7, 28, 13, 30, 0))
			.member(MemberEntity.builder().id(1L).name("nathan").build())
			.rallyPlace(RallyPlaceEntity.builder().id(1L)
				.address(
					Address.builder().city("서울시").district("강남구").roadNameAddress("서울시 강남구 12-1").build()
				).name("송실내테니스").build()
			)
			.build();
		RallyScheduleEntity rallySchedule2 = RallyScheduleEntity.builder()
			.id(2L)
			.playTime(60)
			.startTime(LocalDateTime.of(2023, 8, 21, 13, 30, 0))
			.member(MemberEntity.builder().id(2L).name("hjun").build())
			.rallyPlace(RallyPlaceEntity.builder().id(1L)
				.address(
					Address.builder().city("서울시").district("마포구").roadNameAddress("서울시 마포구 구룡길 11-1").build()
				).name("상암 실내테니스").build()
			)
			.build();
		var searchDTO = RallyScheduleSearchDTO.builder()
																.city("서울시")
																.district("마포구")
																.build();
		var rallyScheduleEntities = List.of(rallySchedule2);
		when(rallyScheduleRepository.findAll()).thenReturn(rallyScheduleEntities);


		var rallyScheduleResponseDTOS = sut.getRallySchedules(searchDTO);


		assertEquals(1, rallyScheduleResponseDTOS.size());
		assertEquals(rallyScheduleEntities.get(0).getRallyPlace().getAddress().getCity(), rallyScheduleResponseDTOS.get(0).city());
    }
}
