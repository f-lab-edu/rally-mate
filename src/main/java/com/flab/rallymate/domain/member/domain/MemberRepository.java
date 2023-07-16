package com.flab.rallymate.domain.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flab.rallymate.domain.member.constant.MemberStatus;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	Optional<MemberEntity> findMemberByEmailAndMemberStatus(String email, MemberStatus status);
	Optional<MemberEntity> findByIdAndMemberStatus(Long id, MemberStatus status);
}
