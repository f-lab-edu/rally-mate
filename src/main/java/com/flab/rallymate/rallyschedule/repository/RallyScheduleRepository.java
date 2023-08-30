package com.flab.rallymate.rallyschedule.repository;

import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;
import com.flab.rallymate.rallyschedule.store.RallyScheduleQueryStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RallyScheduleRepository extends JpaRepository<RallyScheduleEntity, Long>, RallyScheduleQueryStore {

}
