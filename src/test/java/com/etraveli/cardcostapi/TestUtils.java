package com.etraveli.cardcostapi;

import com.etraveli.cardcostapi.dtos.CostDTO;
import com.etraveli.cardcostapi.dtos.CostRedisDTO;
import com.etraveli.cardcostapi.entity.Cost;

import java.util.List;

/**
 * Helper class for testing utilities
 */
public class TestUtils {

    public static List<Cost> createCountryCostRecords() {
        Cost usa = new Cost();
        usa.setId(1);
        usa.setCountry("US");
        usa.setCost(5.00);

        Cost greece = new Cost();
        greece.setId(2);
        greece.setCountry("GR");
        greece.setCost(15.00);

        Cost other = new Cost();
        other.setId(3);
        other.setCost(10.00);
        other.setCountry("Others");

        return List.of(usa, greece, other);
    }

    public static CostDTO createCountryCostRecord() {
        CostDTO cost = new CostDTO();
        cost.setCountry("GR");
        cost.setCost(15.00);

        return cost;
    }

    public static CostRedisDTO createRedisCountryCost() {
        CostRedisDTO costRedisEntity = new CostRedisDTO();
        costRedisEntity.setCountry("GR");
        costRedisEntity.setCost(15.00);

        return costRedisEntity;
    }
}

