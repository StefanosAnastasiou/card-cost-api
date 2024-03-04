package com.etraveli.cardcostapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Entity Object for Redis
 */
@Entity
@Builder
@Data
@RedisHash("countryCost")
public class CostRedisEntity implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String country;
    private Double cost;

    public CostRedisEntity() {
    }

    public CostRedisEntity(Long id, String country, Double cost) {
        this.id = id;
        this.country = country;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("Values{country=%s, cost=%.2f}", country, cost);
    }
}
