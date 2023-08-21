package com.flab.rallymate.rallyschedule.domain.dto;

import com.flab.rallymate.rallyschedule.domain.RallyScheduleEntity;
import lombok.Builder;

import java.time.LocalDateTime;

public record RallyScheduleResponseDTO(
    Long id,
    String placeName,
    String city,
    String district,
    LocalDateTime startTime,
    Integer playTime,
    String memberName
) {
    @Builder
    public RallyScheduleResponseDTO {
    }

    public static RallyScheduleResponseDTO toRallyScheduleResponseDTO(RallyScheduleEntity rallySchedule) {
        return RallyScheduleResponseDTO.builder()
            .id(rallySchedule.getId())
            .placeName(rallySchedule.getRallyPlace().getName())
            .city(rallySchedule.getRallyPlace().getAddress().getCity())
            .district(rallySchedule.getRallyPlace().getAddress().getDistrict())
            .startTime(rallySchedule.getStartTime())
            .playTime(rallySchedule.getPlayTime())
            .memberName(rallySchedule.getMember().getName())    // N+1?
            .build();
    }
}
