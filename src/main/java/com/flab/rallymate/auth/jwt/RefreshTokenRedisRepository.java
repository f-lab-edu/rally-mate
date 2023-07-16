package com.flab.rallymate.auth.jwt;

import org.springframework.data.repository.CrudRepository;

import com.flab.rallymate.auth.jwt.dto.RefreshTokenEntity;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenEntity, String> {
}
