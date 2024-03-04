package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.dtos.CostDTO;
import com.etraveli.cardcostapi.dtos.CostRedisDTO;
import com.etraveli.cardcostapi.entity.Cost;
import com.etraveli.cardcostapi.entity.CostRedisEntity;
import com.etraveli.cardcostapi.exceptions.CountryExistsException;
import com.etraveli.cardcostapi.repository.JpaClearingCostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import mappers.CostMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClearingCostServiceTest {
    @Mock
    private JpaClearingCostRepository jpaClearingCostRepository;
    public static MockWebServer mockBackEnd;
    private ClearingCostService clearingCostService;
    private final String GR = "GR";
    private final String US = "US";

    @BeforeEach
    void initialize() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());

        var webClient = WebClient.builder();
        clearingCostService =
                new ClearingCostService(
                        webClient,
                        baseUrl,
                        jpaClearingCostRepository);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void test_find_all_record() {
        when(jpaClearingCostRepository.findAll()).thenReturn(List.of(new Cost(), new Cost()));
        List<Cost> result = clearingCostService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void test_update_record_by_country_when_country_exists_update_record() {
        Cost cost = new Cost();
        cost.setCountry(US);
        cost.setCost(5.00);

        when(jpaClearingCostRepository.findByCountry(US)).thenReturn(cost);
        int COST = 5;
        when(jpaClearingCostRepository.updateCostByCountry(US, COST)).thenReturn(1);
        int result = clearingCostService.updateCostByCountry(US, COST);

        assertEquals(1, result);
    }

    @Test
    void test_update_record_by_country_when_country_does_not_exist_should_throw_exception() {
        Cost cost = new Cost();
        cost.setCountry(GR);
        cost.setCost(15.00);
        when(jpaClearingCostRepository.findByCountry(US)).thenReturn(cost);

        assertThrows(CountryExistsException.class, () -> clearingCostService.save(US, 50));
    }

    @Test
    void test_country_exists() {
        when(jpaClearingCostRepository.findByCountry(GR)).thenReturn(new Cost());
        boolean result = clearingCostService.countryExists(GR);

        assertTrue(result);
    }

    @Test
    void test_delete_by_country() {
        Cost cost = new Cost();
        cost.setCountry(GR);
        when(jpaClearingCostRepository.findByCountry(GR)).thenReturn(cost);

        clearingCostService.deleteByCountry(GR);

        verify(jpaClearingCostRepository, times(1)).deleteByCountry(GR);
    }

    @Test
    void test_save_record() {
        when(jpaClearingCostRepository.findByCountry("DK")).thenReturn(null);
        Cost savedCost = new Cost();
        savedCost.setCountry("DK");
        savedCost.setCost(50.00);
        when(jpaClearingCostRepository.save(any(Cost.class))).thenReturn(savedCost);

        CostDTO result = clearingCostService.save("DK", 50);

        assertNotNull(result);
        assertEquals("DK", result.getCountry());
        assertEquals(50, result.getCost());
    }

    @Test
    void test_process_response_to_deserializer() throws JsonProcessingException {
        String body = "{\n" +
                "  \"number\": {},\n" +
                "  \"scheme\": \"mastercard\",\n" +
                "  \"type\": \"credit\",\n" +
                "  \"brand\": \"Mastercard Credit Card (Mixed Bin)\",\n" +
                "  \"country\": {\n" +
                "    \"numeric\": \"300\",\n" +
                "    \"alpha2\": \"GR\",\n" +
                "    \"name\": \"Greece\",\n" +
                "    \"emoji\": \"\uD83C\uDDEC\uD83C\uDDF7\",\n" +
                "    \"currency\": \"EUR\",\n" +
                "    \"latitude\": 39,\n" +
                "    \"longitude\": 22\n" +
                "  },\n" +
                "  \"bank\": {\n" +
                "    \"name\": \"Eurobank Ergasias S.A.\"\n" +
                "  }\n" +
                "}";

        CostRedisDTO result = clearingCostService.process(body);

        assertNotNull(result);
    }
}
