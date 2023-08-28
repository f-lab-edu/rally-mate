package com.flab.rallymate.rallyschedule.store;

import com.flab.rallymate.rallyschedule.domain.RallyScheduleEntity;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleSearchDTO;

import java.util.List;

public interface RallyScheduleQueryStore {

    List<RallyScheduleEntity> getRallySchedules(RallyScheduleSearchDTO searchDTO);
}
