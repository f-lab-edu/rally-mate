package com.flab.rallymate.rallyschedule;

import static com.flab.rallymate.error.ErrorCode.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.rallymate.error.BaseException;
import com.flab.rallymate.member.MemberService;
import com.flab.rallymate.rallyplace.RallyPlaceService;
import com.flab.rallymate.rallyschedule.domain.RallyScheduleEntity;
import com.flab.rallymate.rallyschedule.domain.RallyScheduleSpecification;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleRequestDTO;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleResponseDTO;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleSearchDTO;
import com.flab.rallymate.rallyschedule.repository.RallyScheduleRepository;

import lombok.RequiredArgsConstructor;

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

		return rallyScheduleRepository.findAllBy(searchDTO)
			.stream()
			.map(RallyScheduleResponseDTO::toRallyScheduleResponseDTO)
			.collect(Collectors.toList());
	}

	private Specification<RallyScheduleEntity> getRallyScheduleSearchPredicate(RallyScheduleSearchDTO searchDTO) {
		Specification<RallyScheduleEntity> spec = (root, query, criteriaBuilder) -> null;

		if (searchDTO.city() != null) {
			spec = spec.and(RallyScheduleSpecification.withCity(searchDTO.city()));
		}
		if (searchDTO.district() != null) {
			spec = spec.and(RallyScheduleSpecification.withDistrict(searchDTO.district()));
		}
		if (searchDTO.placeName() != null) {
			spec = spec.and(RallyScheduleSpecification.withPlaceName(searchDTO.placeName()));
		}
		if (searchDTO.startTime() != null) {
			spec = spec.and(RallyScheduleSpecification.withStartTime(searchDTO.startTime()));
		}

		return spec;
	}
}
