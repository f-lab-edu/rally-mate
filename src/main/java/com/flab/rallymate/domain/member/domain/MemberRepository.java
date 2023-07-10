package com.flab.rallymate.domain.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flab.rallymate.domain.member.constant.Status;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findMemberByEmailAndStatus(String email, Status status);
	Optional<Member> findByIdAndStatus(Long id, Status status);
}
