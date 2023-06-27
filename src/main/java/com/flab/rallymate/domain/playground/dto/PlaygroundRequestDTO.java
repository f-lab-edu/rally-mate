package com.flab.rallymate.domain.playground.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record PlaygroundRequestDTO(
        @NotBlank(message = "플레이공간 이름을 입력해주세요.")
        String name,
        String city,
        String district,
        String roadNameAddress
) {
    @Builder
    public PlaygroundRequestDTO {
    }

    public static PlaygroundRequestDTO of(String name, String city, String district, String roadNameAddress) {
        return new PlaygroundRequestDTO(name, city, district, roadNameAddress);
    }

}