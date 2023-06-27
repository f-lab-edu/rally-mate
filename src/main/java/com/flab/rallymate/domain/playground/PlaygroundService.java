package com.flab.rallymate.domain.playground;

import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.member.domain.Member;
import com.flab.rallymate.domain.playground.domain.Playground;
import com.flab.rallymate.domain.playground.domain.PlaygroundRepository;
import com.flab.rallymate.domain.playground.dto.PlaygroundRequestDTO;
import com.flab.rallymate.error.BaseException;
import com.flab.rallymate.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaygroundService {

    private final MemberService memberService;
    private final PlaygroundRepository playgroundRepository;

    public void addPlace(PlaygroundRequestDTO playgroundRequestDTO) {

        String memberEmail = memberService.getCurrentMemberEmail();
        Member findMember = memberService.findMemberBy(memberEmail).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MEMBER));

        Playground playground = Playground.from(playgroundRequestDTO);
        playground.changeMember(findMember);

        playgroundRepository.save(playground);
    }

}
