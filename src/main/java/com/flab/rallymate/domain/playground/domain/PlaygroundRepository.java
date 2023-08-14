package com.flab.rallymate.domain.playground.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaygroundRepository extends JpaRepository<PlaygroundEntity, Long> {
}