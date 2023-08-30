package com.flab.rallymate.rallyschedule.store;

import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleSearchDTO;
import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.flab.rallymate.rallyplace.domain.QRallyPlaceEntity.rallyPlaceEntity;
import static com.flab.rallymate.rallyschedule.domain.entity.QRallyScheduleEntity.rallyScheduleEntity;

@Repository
@RequiredArgsConstructor
public class RallyScheduleQueryStoreImpl implements RallyScheduleQueryStore {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RallyScheduleEntity> getRallySchedules(RallyScheduleSearchDTO searchDTO) {
        return queryFactory
                .selectFrom(rallyScheduleEntity)
                .join(rallyScheduleEntity.rallyPlace, rallyPlaceEntity)
                .where(cityEq(searchDTO.city()),
                        districtEq(searchDTO.district()),
                        startTimeAfter(searchDTO.startTime()),
                        placeNameLike(searchDTO.placeName()))
                .fetch();
    }

    private BooleanExpression cityEq(String cityCond) {
        return cityCond != null ? rallyScheduleEntity.rallyPlace.address.city.eq(cityCond) : null;
    }

    private BooleanExpression districtEq(String districtCond) {
        return districtCond != null ? rallyScheduleEntity.rallyPlace.address.district.eq(districtCond) : null;
    }

    private BooleanExpression startTimeAfter(LocalDateTime startTimeCond) {
        return startTimeCond != null ? rallyScheduleEntity.startTime.after(startTimeCond) : null;
    }

    private BooleanExpression placeNameLike(String placeNameCond) {
        return placeNameCond != null ? rallyScheduleEntity.rallyPlace.name.contains(placeNameCond) : null;
    }
}
