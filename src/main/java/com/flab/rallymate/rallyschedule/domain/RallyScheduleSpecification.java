package com.flab.rallymate.rallyschedule.domain;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

public class RallyScheduleSpecification {
	public static Specification<RallyScheduleEntity> withPlaceName(String placeName) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("rallyPlace").get("name"),
			"%" + placeName + "%");
	}

	public static Specification<RallyScheduleEntity> withCity(String city) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("rallyPlace").get("address").get("city"),
			"%" + city + "%");
	}

	public static Specification<RallyScheduleEntity> withDistrict(String district) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(
			root.get("rallyPlace").get("address").get("district"), "%" + district + "%");
	}

	public static Specification<RallyScheduleEntity> withStartTime(LocalDateTime startTime) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), startTime);
	}
}
