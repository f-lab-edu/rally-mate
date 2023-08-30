package com.flab.rallymate.rallyschedule.domain.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record RallyScheduleSearchDTO(
    String city,
    String district,
    LocalDateTime startTime,
    String placeName
) {
    @Builder
    public RallyScheduleSearchDTO {
    }
}
