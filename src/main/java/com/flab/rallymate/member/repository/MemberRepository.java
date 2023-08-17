package com.flab.rallymate.member.repository;

import java.util.Optional;

import com.flab.rallymate.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flab.rallymate.member.enums.MemberStatus;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	Optional<MemberEntity> findMemberByEmailAndMemberStatus(String email, MemberStatus status);
	Optional<MemberEntity> findByIdAndMemberStatus(Long id, MemberStatus status);
}
