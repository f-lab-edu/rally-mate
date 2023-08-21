package com.flab.rallymate.rallyschedule.domain;

import com.flab.rallymate.rallyschedule.domain.dto.RallyScheduleSearchDTO;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class RallyScheduleSpecification {
    public static Specification<RallyScheduleSearchDTO> withPlaceName(String placeName) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("rallyPlace").get("name"), placeName);
    }

    public static Specification<RallyScheduleSearchDTO> withCity(String city) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("rallyPlace").get("address").get("city"), city);
    }

    public static Specification<RallyScheduleSearchDTO> withDistrict(String district) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("rallyPlace").get("address").get("district"), district);
    }

    public static Specification<RallyScheduleSearchDTO> withStartTime(LocalDateTime startTime) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), startTime);
    }
}
