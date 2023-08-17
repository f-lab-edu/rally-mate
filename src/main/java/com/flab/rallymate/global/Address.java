package com.flab.rallymate.global;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Address {

    private String city;

    private String district;

    @Column(name = "road_name_address")
    private String roadNameAddress;


    private Address(String city, String district, String roadNameAddress) {
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