package com.flab.rallymate.rallyplace.domain;

import com.flab.rallymate.global.Address;
import com.flab.rallymate.global.BaseEntity;
import com.flab.rallymate.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rally_place")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RallyPlaceEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    private RallyPlaceEntity(Long id, String name, Address address, MemberEntity member) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.member = member;
    }
}