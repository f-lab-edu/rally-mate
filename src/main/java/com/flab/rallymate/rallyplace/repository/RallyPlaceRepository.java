package com.flab.rallymate.rallyplace.repository;

import com.flab.rallymate.rallyplace.domain.RallyPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RallyPlaceRepository extends JpaRepository<RallyPlaceEntity, Long> {
}