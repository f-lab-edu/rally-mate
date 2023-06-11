package com.flab.rallymate.domain.member;

import static com.flab.rallymate.common.response.ErrorCode.*;
import static com.flab.rallymate.domain.member.constant.Status.*;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.rallymate.common.response.BaseException;
import com.flab.rallymate.common.util.jwt.JwtTokenProvider;
import com.flab.rallymate.domain.member.domain.Member;
import com.flab.rallymate.domain.member.domain.MemberRepository;
import com.flab.rallymate.domain.member.dto.MemberDTO;
import com.flab.rallymate.domain.member.dto.MemberJoinReq;
import com.flab.rallymate.domain.member.dto.MemberLoginReq;
import com.flab.rallymate.domain.member.dto.MemberLoginRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider tokenProvider;

	public MemberService(MemberRepository memberRepository,
		PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenProvider = tokenProvider;
	}

	public MemberLoginRes login(MemberLoginReq loginReq) {

		Member findMember = memberRepository.findMemberByEmailAndStatus(loginReq.email(), USED)
			.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		log.info("### encoded password = {}", passwordEncoder.encode("1234"));

		if (!passwordEncoder.matches(loginReq.password(), findMember.getPassword()))
			throw new BaseException(PASSWORD_NOT_MATCH);

		return MemberLoginRes.of(findMember.getId(), tokenProvider.createAccessToken(findMember));
	}

	@Transactional
	public Long join(MemberJoinReq joinReq) {
		String password = passwordEncoder.encode(joinReq.password());
		Member member = Member.createMember(joinReq.name(), joinReq.email(), password);

		Member savedMember = memberRepository.save(member);
		return savedMember.getId();
	}

	public Optional<MemberDTO> findMemberBy(String email) {
		return memberRepository.findMemberByEmailAndStatus(email, USED)
			.map(MemberDTO::from);
	}

}
