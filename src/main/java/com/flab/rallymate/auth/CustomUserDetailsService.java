package com.flab.rallymate.auth;

import static com.flab.rallymate.error.ErrorCode.*;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.flab.rallymate.auth.model.CustomUserDetails;
import com.flab.rallymate.domain.member.constant.MemberStatus;
import com.flab.rallymate.domain.member.domain.MemberEntity;
import com.flab.rallymate.domain.member.domain.MemberRepository;
import com.flab.rallymate.error.BaseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		MemberEntity member = memberRepository.findMemberByEmailAndMemberStatus(email, MemberStatus.ACTIVATE)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));
		return new CustomUserDetails(member.getEmail(), member.getPassword(), List.of(member.getUserRole().getValue()));
	}
}
