package com.etraveli.cardcostapi.controller;

import com.etraveli.cardcostapi.dtos.CostDTO;
import com.etraveli.cardcostapi.dtos.CostRedisDTO;
import com.etraveli.cardcostapi.entity.Cost;
import com.etraveli.cardcostapi.service.ClearingCostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * RestController responsible for fetching values from lookup table and performing CRUD operations in database
 */
@RestController
@RequestMapping("cost-clearance")
public class CostClearanceController {
    private static final Logger logger = LoggerFactory.getLogger(CostClearanceController.class);
    private final ClearingCostService clearingCostService;

    public CostClearanceController(ClearingCostService clearingCostService) {
        this.clearingCostService = clearingCostService;
    }

    /**
     * Fetches all available records from database.
     *
     * @return a List of records
     */
    @GetMapping("/get-records")
    public ResponseEntity<List<Cost>> getAllRecords() {
        logger.info("Fetching all records");

        return ResponseEntity.ok(clearingCostService.findAll());
    }

    /**
     * Gets a record by country
     *
     * @param country the country
     * @return Cost entity
     */
    @Cacheable(value = "countryCost", key = "#country")
    @GetMapping("/get-by-country/{country}")
    public CostDTO getRecordsByCountry(@PathVariable String country) {
        logger.info("Fetching record for country {} ", country);

        return clearingCostService.findByCountry(country);
    }

    /**
     * Adds a new record to the database
     *
     * @param country the country inserted
     * @param cost    the cost inserted
     * @return a Cost entity
     */
    @PostMapping("/{country}/{cost}")
    public ResponseEntity<CostDTO> insertCountry(@PathVariable String country, @PathVariable double cost) {
        logger.info("Inserting country {} with value {} ", country, cost);

        return ResponseEntity.ok(clearingCostService.save(country, cost));
    }

    /**
     * Updates the cost for a specified country.
     *
     * @param country the country to be updated.
     * @param cost    the cost the country is updated with
     * @return int value
     */
    @PutMapping("/{country}/{cost}")
    public ResponseEntity<Integer> updateByCountry(@PathVariable String country, @PathVariable double cost) {
        logger.info("Updating cost for country {} with value {}, ", country, cost);

        return ResponseEntity.ok(clearingCostService.updateCostByCountry(country, cost));
    }

    /**
     * Deletes a record from the database with the specified country as input.
     *
     * @param country the country to be deleted
     */
    @DeleteMapping("{country}")
    public void deleteByCountry(@PathVariable String country) {
        logger.info("Deleting record for country {} ", country);

        clearingCostService.deleteByCountry(country);
    }


    /**
     * Requests <a href="URL#vhttps://binlist.net/">binlist.net</a> for country and cost information
     *
     * @param cardNumber the card number to
     * @return the entity for country and cost
     * @throws JsonProcessingException
     */
    @Cacheable(value = "countryCost", key = "#cardNumber")
    @GetMapping(path = "/{cardNumber}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CostRedisDTO getCardCost(@PathVariable String cardNumber) throws JsonProcessingException {
        logger.info("Fetching card information from binlist external API.");
        String result = clearingCostService.getBinListTableCost(cardNumber);

        return clearingCostService.process(result);
    }
}
