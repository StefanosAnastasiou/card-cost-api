package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.dtos.CostDTO;
import com.etraveli.cardcostapi.dtos.CostRedisDTO;
import com.etraveli.cardcostapi.entity.Cost;
import com.etraveli.cardcostapi.entity.CostRedisEntity;
import com.etraveli.cardcostapi.exceptions.*;
import com.etraveli.cardcostapi.repository.JpaClearingCostRepository;
import com.etraveli.cardcostapi.utils.CountryCostDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import mappers.CostMapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service class
 */
@Service
public class ClearingCostService {
    private final Logger logger = LoggerFactory.getLogger(ClearingCostService.class);
    private final JpaClearingCostRepository jpaClearingCostRepository;
    private final WebClient webClient;
    private final CostMapper costMapper;
    private final String ERROR_MESSAGE = "A error occurred while retrieving information";

    public ClearingCostService(
            WebClient.Builder webClientBuilder, @Value("${binlist.host}") String binLookupHost,
            JpaClearingCostRepository jpaClearingCostRepository
    ) {
        this.jpaClearingCostRepository = jpaClearingCostRepository;
        this.webClient = webClientBuilder.baseUrl(binLookupHost).build();
        this.costMapper = Mappers.getMapper(CostMapper.class);
    }

    /**
     * Gets the record for a specified country
     *
     * @param country the country
     * @return the CostDTO
     */
    @Transactional(readOnly = true)
    public CostDTO findByCountry(String country) {
        if (jpaClearingCostRepository.findByCountry(country) == null) {
            logger.info("Country {} does not exist", country);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        }
        Cost cost = jpaClearingCostRepository.findByCountry(country);

        return costMapper.entityToDTO(cost);
    }

    /**
     * Fetches all available records from database.
     *
     * @return all records from database
     */
    @Transactional(readOnly = true)
    public List<Cost> findAll() {
        return (List) jpaClearingCostRepository.findAll();
    }

    /**
     * Updates the cost of a specified country.
     *
     * @param country the country
     * @param cost    the cost to be updated
     * @return int
     */
    @Transactional
    public int updateCostByCountry(String country, double cost) {
        if (!countryExists(country)) {
            logger.error("The country you are trying to update does not exist");
            throw new CountryNotExistsException();
        }
        return jpaClearingCostRepository.updateCostByCountry(country, cost);
    }

    @Transactional(readOnly = true)
    public boolean countryExists(String country) {
        return jpaClearingCostRepository.findByCountry(country) != null;
    }

    /**
     * Deletes a record in the database by the specified country.
     *
     * @param country the input country
     */
    @Transactional
    public void deleteByCountry(String country) {
        Cost cost = jpaClearingCostRepository.findByCountry(country);
        if (cost.getCountry().equals("Others")) {
            logger.error("'Others' record is a default price. Cannot be deleted");
            throw new DeleteDefaultValueException();
        }
        jpaClearingCostRepository.deleteByCountry(country);
    }

    /**
     * Inserts a new country and cost into the database
     *
     * @param country the country name
     * @param value   the cost
     * @return a Cost entity
     */
    @Transactional
    public CostDTO save(@PathVariable String country, @PathVariable double value) {
        if (countryExists(country)) {
            logger.error("The country you are trying to insert already exists in the database");
            throw new CountryExistsException();
        }
        Cost cost = new Cost();
        cost.setCountry(country);
        cost.setCost(value);

        return costMapper.entityToDTO(jpaClearingCostRepository.save(cost));
    }

    /**
     * Requests <a href="URL#vhttps://binlist.net/">binlist.net</a> external API for with a specified IIN Number
     *
     * @param cardNumber the INN number
     * @return the String result
     */
    @Transactional
    public String getBinListTableCost(String cardNumber) {
        if (cardNumber.length() < 6 || cardNumber.length() > 8) {
            logger.error("The card number must not be more than 6 digits");
            throw new IllegalCardLengthException();
        }
        if (containsCharacter(cardNumber)) {
            logger.error("The card number must only contains digits");
            throw new IllegalCardFormatException();
        }
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder.pathSegment("{binNumber}").build(cardNumber))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(cardNumber), String.class)
                .retrieve()
                .onStatus(
                        status -> status.value() == HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        response -> Mono.error(new ErrorException(ERROR_MESSAGE))
                )
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Processes and deserializes the response to CostRedisEntity
     *
     * @param body the response body from <a href="URL#vhttps://binlist.net/">binlist.net</a> external API
     * @return Entity
     * @throws JsonProcessingException
     */
    public CostRedisDTO process(String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(CostRedisEntity.class, new CountryCostDeserializer());
        mapper.registerModule(module);

        CostRedisEntity costRedisEntity = mapper.readValue(body, CostRedisEntity.class);

        return costMapper.redisEntityToDTO(costRedisEntity);
    }

    /**
     * Checks if a given String contains characters
     *
     * @param s the String
     * @return true or false
     */
    private boolean containsCharacter(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = !Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }
}
