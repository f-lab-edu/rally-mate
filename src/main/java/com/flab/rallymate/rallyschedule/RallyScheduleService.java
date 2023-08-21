package com.flab.rallymate.rallyschedule;

import com.flab.rallymate.member.MemberService;
import com.flab.rallymate.rallyplace.RallyPlaceService;
import com.flab.rallymate.rallyschedule.domain.RallyScheduleSpecification;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleResponseDTO;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleSearchDTO;
import com.flab.rallymate.rallyschedule.repository.RallyScheduleRepository;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleRequestDTO;
import com.flab.rallymate.error.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
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

        return Optional.of(rallyScheduleRepository.findAllFetchJoin(getRallyScheduleSearchPredicate(searchDTO)))
                .orElseGet(Collections::emptyList)
                .stream()
                .map(RallyScheduleResponseDTO::toRallyScheduleResponseDTO)
                .collect(Collectors.toList());
    }

    public Specification<RallyScheduleSearchDTO> getRallyScheduleSearchPredicate(RallyScheduleSearchDTO searchDTO) {
        Specification<RallyScheduleSearchDTO> rallyScheduleSearchDTOSpecification = (root, query, criteriaBuilder) -> null;

        if(searchDTO.city() != null) {
            rallyScheduleSearchDTOSpecification = rallyScheduleSearchDTOSpecification
                    .and(RallyScheduleSpecification.withCity(searchDTO.city()));
        }
        if(searchDTO.district() != null) {
            rallyScheduleSearchDTOSpecification = rallyScheduleSearchDTOSpecification
                    .and(RallyScheduleSpecification.withDistrict(searchDTO.district()));
        }
        if(searchDTO.placeName() != null) {
            rallyScheduleSearchDTOSpecification = rallyScheduleSearchDTOSpecification
                    .and(RallyScheduleSpecification.withPlaceName(searchDTO.placeName()));
        }
        if(searchDTO.startTime() != null) {
            rallyScheduleSearchDTOSpecification = rallyScheduleSearchDTOSpecification
                    .and(RallyScheduleSpecification.withStartTime(searchDTO.startTime()));
        }

        return rallyScheduleSearchDTOSpecification;
    }
}