package com.flab.rallymate.rallyschedule.domain.dto;

import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.rallyplace.domain.RallyPlaceEntity;
import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record RallyScheduleRequestDTO(
        @NotNull(message = "플레이 공간을 선택해 주세요.")
        Long rallyPlaceId,

        @Positive(message = "플레이 시간은 0보다 큽니다.")
        @NotNull(message = "플레이 시간을 입력해 주세요.")
        Integer playTime,

        @NotNull(message = "시작 시간을 입력해 주세요.")
        @Future(message = "현재 시간 이후로 입력해 주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startTime
) {
    @Builder
    public RallyScheduleRequestDTO {
    }

    public RallyScheduleEntity toRallyScheduleEntity(MemberEntity member, RallyPlaceEntity rallyPlace) {
        return RallyScheduleEntity.builder()
                .playTime(playTime())
                .startTime(startTime())
                .member(member)
                .rallyPlace(rallyPlace)
                .build();
    }
}
