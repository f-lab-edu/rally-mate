package com.flab.rallymate.rallyschedule;

import com.flab.rallymate.StoreTest;
import com.flab.rallymate.config.QuerydslConfig;
import com.flab.rallymate.global.Address;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.member.enums.UserRole;
import com.flab.rallymate.rallyplace.domain.RallyPlaceEntity;
import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;
import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleSearchDTO;
import com.flab.rallymate.rallyschedule.store.RallyScheduleQueryStore;
import com.flab.rallymate.rallyschedule.store.RallyScheduleQueryStoreImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@ActiveProfiles("test")
@Transactional
@Import({QuerydslConfig.class})
public class RallyScheduleStoreTest extends StoreTest {

    @PersistenceContext
    EntityManager entityManager;

    RallyScheduleQueryStore sut;


    @BeforeEach
    void setUp() {
        sut = new RallyScheduleQueryStoreImpl(new JPAQueryFactory(entityManager));
    }

    @Test
    void getRallySchedules_서울시_마포구에_해당하는_랠리_스케쥴_반환에_성공한다() {
        var member = getMember();
        entityManager.persist(member);

        var rallyPlaceEntityList = getRallyPlaces(member);
        entityManager.persist(rallyPlaceEntityList.get(0));
        entityManager.persist(rallyPlaceEntityList.get(1));
        entityManager.persist(rallyPlaceEntityList.get(2));

        var rallyScheduleEntityList = getScheduleEntities(rallyPlaceEntityList);
        entityManager.persist(rallyScheduleEntityList.get(0));
        entityManager.persist(rallyScheduleEntityList.get(1));
        entityManager.persist(rallyScheduleEntityList.get(2));

        flushAndClear(entityManager);


        var result = sut.getRallySchedules(RallyScheduleSearchDTO.builder()
                .city("서울시")
                .district("마포구")
                .build());

        var item = result.get(0);
        assertEquals(1, result.size());
        assertEquals("서울시", item.getRallyPlace().getAddress().getCity());
        assertEquals("마포구", item.getRallyPlace().getAddress().getDistrict());
    }

    @Test
    void getRallySchedules_특정_시작시간_이후의_랠리_스케쥴_반환에_성공한다() {
        var member = getMember();
        entityManager.persist(member);

        var rallyPlaceEntityList = getRallyPlaces(member);
        entityManager.persist(rallyPlaceEntityList.get(0));
        entityManager.persist(rallyPlaceEntityList.get(1));
        entityManager.persist(rallyPlaceEntityList.get(2));

        var rallyScheduleEntityList = getScheduleEntities(rallyPlaceEntityList);
        entityManager.persist(rallyScheduleEntityList.get(0));
        entityManager.persist(rallyScheduleEntityList.get(1));
        entityManager.persist(rallyScheduleEntityList.get(2));

        flushAndClear(entityManager);


        var searchDTO = RallyScheduleSearchDTO.builder()
                .startTime(LocalDateTime.of(2023, 8, 1, 0, 0))
                .build();
        var result = sut.getRallySchedules(searchDTO);

        var item = result.get(0);
        assertEquals(2, result.size());
        assertTrue( item.getStartTime().isAfter(searchDTO.startTime()));

    }

    @Test
    void getRallySchedules_장소명에_실내와_LIKE_매치되는_랠리_스케쥴_반환에_성공한다() {
        var member = getMember();
        entityManager.persist(member);

        var rallyPlaceEntityList = getRallyPlaces(member);
        entityManager.persist(rallyPlaceEntityList.get(0));
        entityManager.persist(rallyPlaceEntityList.get(1));
        entityManager.persist(rallyPlaceEntityList.get(2));

        var rallyScheduleEntityList = getScheduleEntities(rallyPlaceEntityList);
        entityManager.persist(rallyScheduleEntityList.get(0));
        entityManager.persist(rallyScheduleEntityList.get(1));
        entityManager.persist(rallyScheduleEntityList.get(2));

        flushAndClear(entityManager);


        var result = sut.getRallySchedules(RallyScheduleSearchDTO.builder()
                .placeName("실내")
                .build());

        var item = result.get(0);
        assertEquals(2, result.size());
        assertTrue(item.getRallyPlace().getName().contains("실내"));
    }



    private List<RallyScheduleEntity> getScheduleEntities(List<RallyPlaceEntity> rallyPlaceEntityList) {

		var rallySchedule1 = RallyScheduleEntity.builder()
			.playTime(120)
			.startTime(LocalDateTime.of(2023, 7, 28, 13, 30, 0))
			.member(rallyPlaceEntityList.get(0).getMember())
			.rallyPlace(rallyPlaceEntityList.get(0))
			.build();

		var rallySchedule2 = RallyScheduleEntity.builder()
			.playTime(60)
			.startTime(LocalDateTime.of(2023, 8, 21, 13, 30, 0))
			.member(rallyPlaceEntityList.get(1).getMember())
			.rallyPlace(rallyPlaceEntityList.get(1))
			.build();

        var rallySchedule3 = RallyScheduleEntity.builder()
      			.playTime(200)
      			.startTime(LocalDateTime.of(2023, 10, 10, 10, 0, 0))
      			.member(rallyPlaceEntityList.get(2).getMember())
      			.rallyPlace(rallyPlaceEntityList.get(2))
      			.build();

        return List.of(rallySchedule1, rallySchedule2, rallySchedule3);
    }

    private List<RallyPlaceEntity> getRallyPlaces(MemberEntity member) {
        var rallyPlaceEntity1 = RallyPlaceEntity.builder()
                .address(
                        Address.builder().city("서울시").district("강남구").roadNameAddress("서울시 강남구 12-1").build()
                ).name("송실내테니스").member(member).build();
        var rallyPlaceEntity2 = RallyPlaceEntity.builder()
                .address(
                    Address.builder().city("서울시").district("마포구").roadNameAddress("서울시 마포구 구룡길 11-1").build()
                ).name("상암 실내테니스").member(member).build();

        var rallyPlaceEntity3 = RallyPlaceEntity.builder()
                .address(
                    Address.builder().city("서울시").district("은평구").roadNameAddress("서울시 은평구 통일로 11-2").build()
                ).name("은평 오픈테니스24").build();

        return List.of(rallyPlaceEntity1, rallyPlaceEntity2, rallyPlaceEntity3);
    }

    private MemberEntity getMember() {
        return MemberEntity.builder()
                .name("네이선")
                .email("test@test.com")
                .password("1234")
                .career(0)
                .userRole(UserRole.ROLE_USER)
                .build();
    }
}
