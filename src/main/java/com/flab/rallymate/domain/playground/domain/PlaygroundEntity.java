package com.flab.rallymate.domain.playground.domain;

import com.flab.rallymate.domain.member.domain.MemberEntity;
import com.flab.rallymate.domain.playground.dto.PlaygroundRequestDTO;
import com.flab.rallymate.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "playground")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaygroundEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    public void changeMember(MemberEntity member) {
        this.member = member;
    }

    @Builder
    public PlaygroundEntity(Long id, String name, Address address, MemberEntity member) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.member = member;
        setCreatedTime(LocalDateTime.now());
    }

    public static PlaygroundEntity from(PlaygroundRequestDTO request) {
        return PlaygroundEntity.builder()
                .name(request.name())
                .address(Address.of(request.city(), request.district(), request.roadNameAddress()))
                .build();
    }
}