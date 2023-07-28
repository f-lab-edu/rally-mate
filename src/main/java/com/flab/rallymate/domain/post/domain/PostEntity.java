package com.flab.rallymate.domain.post.domain;

import com.flab.rallymate.domain.member.domain.MemberEntity;
import com.flab.rallymate.domain.playground.domain.Playground;
import com.flab.rallymate.domain.post.dto.PostRequestDTO;
import com.flab.rallymate.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int playTime;

    private LocalDateTime startTime;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @JoinColumn(name = "playground_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Playground playground;

    public void changeMember(MemberEntity member) {
        this.member = member;
    }

    public void changePlayground(Playground playground) {
        this.playground = playground;
    }

    @Builder
    public PostEntity(int playTime, LocalDateTime startTime, MemberEntity member, Playground playground) {
        this.playTime = playTime;
        this.startTime = startTime;
        this.member = member;
        this.playground = playground;
        setCreatedTime(LocalDateTime.now());
    }

    public static PostEntity from(PostRequestDTO request) {
        return PostEntity.builder()
                .playTime(request.playTime())
                .startTime(request.startTime())
                .build();
    }

}
