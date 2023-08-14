package com.flab.rallymate.domain.playground.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Address {

    private String city;

    private String district;

    @Column(name = "road_name_address")
    private String roadNameAddress;


    @Builder
    public Address(String city, String district, String roadNameAddress) {
        this.city = city;
        this.district = district;
        this.roadNameAddress = roadNameAddress;
    }


    public static Address of(String city, String district, String roadNameAddress) {
        return Address.builder()
                .city(city)
                .district(district)
                .roadNameAddress(roadNameAddress)
                .build();
    }

}