package com.flab.rallymate.domain.post.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record PostRequestDTO(
        @NotNull(message = "플레이 공간을 선택해 주세요.")
        Long playgroundId,

        @Positive(message = "플레이 시간은 0보다 큽니다.")
        @NotNull(message = "플레이 시간을 입력해 주세요.")
        Integer playTime,

        @NotNull(message = "시작 시간을 입력해 주세요.")
        @Future(message = "현재 시간 이후로 입력해 주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startTime
) {
    @Builder
    public PostRequestDTO {
    }
}
