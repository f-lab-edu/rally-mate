package com.flab.rallymate.member;

import com.flab.rallymate.auth.domain.model.CustomUserDetails;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.flab.rallymate.member.enums.MemberStatus.ACTIVATE;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public Optional<MemberEntity> findMemberBy(String email) {
		return memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE);
	}

	public String getCurrentMemberEmail() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
	  		return principal.getUsername();
	}

}
