package com.flab.rallymate.rallyschedule.repository;

import com.flab.rallymate.rallyschedule.domain.RallyScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RallyScheduleRepository extends JpaRepository<RallyScheduleEntity, Long> {
}