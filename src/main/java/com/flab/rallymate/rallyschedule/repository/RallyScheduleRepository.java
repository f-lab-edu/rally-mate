package com.flab.rallymate.rallyschedule.repository;

import com.flab.rallymate.rallyschedule.domain.RallyScheduleEntity;
import com.flab.rallymate.rallyschedule.domain.RallyScheduleSpecification;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleSearchDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RallyScheduleRepository extends JpaRepository<RallyScheduleEntity, Long>
                                                    , JpaSpecificationExecutor<RallyScheduleEntity> {

    @Query("select rs from RallyScheduleEntity rs join fetch rs.member m join fetch rs.rallyPlace rp")
    List<RallyScheduleEntity> findAllFetchJoin(Specification<RallyScheduleSearchDTO> spec);
}