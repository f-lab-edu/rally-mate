package com.flab.rallymate.domain.member;

import com.flab.rallymate.auth.model.CustomUserDetails;
import com.flab.rallymate.domain.member.domain.MemberEntity;
import com.flab.rallymate.domain.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.flab.rallymate.domain.member.constant.MemberStatus.ACTIVATE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public Optional<MemberEntity> findMemberBy(String email) {
		return memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE);
	}

	@Transactional(readOnly = true)
	public Optional<MemberEntity> findMemberBy(Long memberId) {
		return memberRepository.findByIdAndMemberStatus(memberId, ACTIVATE);
	}

	public String getCurrentMemberEmail() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
	  		return principal.getUsername();
	}

}
