package com.etraveli.cardcostapi.repository;

import com.etraveli.cardcostapi.entity.CostRedisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends JpaRepository<CostRedisEntity, Long> {
}
