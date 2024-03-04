package com.etraveli.cardcostapi.repository;

import com.etraveli.cardcostapi.entity.Cost;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface JpaClearingCostRepository extends CrudRepository<Cost, Long> {

    @Query("Select c from Cost c where c.country=?1")
    Cost findByCountry(String country);

    @Modifying
    @Query("update Cost c set c.cost =?2 where c.country=?1")
    int updateCostByCountry(String country, double cost);

    @Modifying
    @Query("delete from Cost c where c.country=?1")
    void deleteByCountry(String country);
}
