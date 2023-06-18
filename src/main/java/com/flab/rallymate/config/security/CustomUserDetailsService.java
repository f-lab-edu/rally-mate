package com.flab.rallymate.config.security;

import static com.flab.rallymate.error.ErrorCode.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.flab.rallymate.domain.member.constant.Status;
import com.flab.rallymate.domain.member.domain.Member;
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
		Member member = memberRepository.findMemberByEmailAndStatus(email, Status.USED)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));
		return new CustomUserDetails(member.getEmail(), member.getPassword(), member.getName());
	}
}
