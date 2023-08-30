package com.flab.rallymate.rallyplace.domain;

import com.flab.rallymate.global.Address;
import com.flab.rallymate.member.domain.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record RallyPlaceRequestDTO(
        @NotBlank(message = "플레이공간 이름을 입력해주세요.")
        String name,
        String city,
        String district,
        String roadNameAddress
) {
    @Builder
    public RallyPlaceRequestDTO {
    }

    public RallyPlaceEntity toRallyPlaceEntity(MemberEntity member) {
        return RallyPlaceEntity.builder()
                .name(name)
                .address(Address.of(city, district, roadNameAddress))
                .member(member)
                .build();
    }

}