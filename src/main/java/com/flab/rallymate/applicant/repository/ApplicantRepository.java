package com.flab.rallymate.applicant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flab.rallymate.applicant.domain.ApplicantEntity;

public interface ApplicantRepository extends JpaRepository<ApplicantEntity, Long> {
}
