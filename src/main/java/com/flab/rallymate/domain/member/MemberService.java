package com.flab.rallymate.domain.member;

import static com.flab.rallymate.domain.member.constant.MemberStatus.*;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.rallymate.domain.member.domain.MemberEntity;
import com.flab.rallymate.domain.member.domain.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

}
