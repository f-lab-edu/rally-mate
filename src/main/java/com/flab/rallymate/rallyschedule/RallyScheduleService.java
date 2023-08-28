package com.flab.rallymate.rallyschedule;

import com.flab.rallymate.error.BaseException;
import com.flab.rallymate.member.MemberService;
import com.flab.rallymate.rallyplace.RallyPlaceService;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleRequestDTO;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleResponseDTO;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleSearchDTO;
import com.flab.rallymate.rallyschedule.repository.RallyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_MEMBER;
import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_PLAYGROUND;

@Service
@Transactional
@RequiredArgsConstructor
public class RallyScheduleService {

	private final RallyScheduleRepository rallyScheduleRepository;
	private final MemberService memberService;
	private final RallyPlaceService rallyPlaceService;

	public void addRallySchedule(RallyScheduleRequestDTO rallyScheduleRequestDTO) {

		String email = memberService.getCurrentMemberEmail();
		var member = memberService.findMemberBy(email).orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		var playground = rallyPlaceService.findRallyPlaceBy(rallyScheduleRequestDTO.playgroundId())
			.orElseThrow(() -> new BaseException(NOT_FOUND_PLAYGROUND));
		var rallySchedule = rallyScheduleRequestDTO.toRallyScheduleEntity(member, playground);
		rallyScheduleRepository.save(rallySchedule);
	}

	@Transactional(readOnly = true)
	public List<RallyScheduleResponseDTO> getRallySchedules(RallyScheduleSearchDTO searchDTO) {

		return rallyScheduleRepository.getRallySchedules(searchDTO)
			.stream()
			.map(RallyScheduleResponseDTO::toRallyScheduleResponseDTO)
			.collect(Collectors.toList());
	}
}
