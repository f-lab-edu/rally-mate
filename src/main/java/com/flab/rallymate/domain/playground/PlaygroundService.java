package com.flab.rallymate.domain.playground;

import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.member.domain.MemberEntity;
import com.flab.rallymate.domain.playground.domain.PlaygroundEntity;
import com.flab.rallymate.domain.playground.domain.PlaygroundRepository;
import com.flab.rallymate.domain.playground.dto.PlaygroundRequestDTO;
import com.flab.rallymate.error.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaygroundService {

    private final MemberService memberService;
    private final PlaygroundRepository playgroundRepository;

    public void addPlace(PlaygroundRequestDTO playgroundRequestDTO) {

        String memberEmail = memberService.getCurrentMemberEmail();
        MemberEntity findMember = memberService.findMemberBy(memberEmail).orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        PlaygroundEntity playground = PlaygroundEntity.from(playgroundRequestDTO);
        playground.changeMember(findMember);

        playgroundRepository.save(playground);
    }


    @Transactional(readOnly = true)
    public Optional<PlaygroundEntity> findPlaygroundBy(Long id) {
        return playgroundRepository.findById(id);
    }

}