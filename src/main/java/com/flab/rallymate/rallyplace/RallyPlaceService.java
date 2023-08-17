package com.flab.rallymate.rallyplace;

import com.flab.rallymate.member.MemberService;
import com.flab.rallymate.rallyplace.domain.RallyPlaceEntity;
import com.flab.rallymate.rallyplace.repository.RallyPlaceRepository;
import com.flab.rallymate.rallyplace.domain.RallyPlaceRequestDTO;
import com.flab.rallymate.error.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional
public class RallyPlaceService {

    private final MemberService memberService;
    private final RallyPlaceRepository rallyPlaceRepository;

    public void addRallyPlace(RallyPlaceRequestDTO rallyPlaceRequestDTO) {

        String memberEmail = memberService.getCurrentMemberEmail();
        var findMember = memberService.findMemberBy(memberEmail).orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        RallyPlaceEntity rallyPlace = rallyPlaceRequestDTO.toRallyPlaceEntity(findMember);
        rallyPlaceRepository.save(rallyPlace);
    }


    @Transactional(readOnly = true)
    public Optional<RallyPlaceEntity> findRallyPlaceBy(Long rallyPlaceId) {
        return rallyPlaceRepository.findById(rallyPlaceId);
    }

}