package com.flab.rallymate.domain.member;

import static com.flab.rallymate.domain.member.constant.Status.*;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.rallymate.domain.member.domain.Member;
import com.flab.rallymate.domain.member.domain.MemberRepository;
import com.flab.rallymate.domain.member.dto.MemberJoinRequestDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public Member join(MemberJoinRequestDTO joinReq) {
		String password = passwordEncoder.encode(joinReq.password());
		Member member = Member.createMember(joinReq.name(), joinReq.email(), password);

		Member savedMember = memberRepository.save(member);
		return savedMember;
	}

	@Transactional(readOnly = true)
	public Optional<Member> findMemberBy(String email) {
		return memberRepository.findMemberByEmailAndStatus(email, USED);
	}

	@Transactional(readOnly = true)
	public Optional<Member> findMemberBy(Long memberId) {
		return memberRepository.findByIdAndStatus(memberId, USED);
	}

}
