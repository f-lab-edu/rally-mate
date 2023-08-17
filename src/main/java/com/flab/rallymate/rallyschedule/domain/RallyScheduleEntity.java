package com.flab.rallymate.rallyschedule.domain;

import com.flab.rallymate.global.BaseEntity;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.rallyplace.domain.RallyPlaceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rally_schedule")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class RallyScheduleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "play_time", nullable = false)
    private int playTime;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @JoinColumn(name = "rally_place_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RallyPlaceEntity playground;

    private RallyScheduleEntity(Long id, int playTime, LocalDateTime startTime, MemberEntity member, RallyPlaceEntity playground) {
        this.id = id;
        this.playTime = playTime;
        this.startTime = startTime;
        this.member = member;
        this.playground = playground;
    }
}