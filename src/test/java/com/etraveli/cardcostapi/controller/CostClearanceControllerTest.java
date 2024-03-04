package com.etraveli.cardcostapi.controller;

import com.etraveli.cardcostapi.TestUtils;
import com.etraveli.cardcostapi.dtos.CostDTO;
import com.etraveli.cardcostapi.dtos.CostRedisDTO;
import com.etraveli.cardcostapi.entity.Cost;
import com.etraveli.cardcostapi.service.ClearingCostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(CostClearanceController.class)
class CostClearanceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClearingCostService clearingCostService;

    private final String GR = "GR";

    @Test
    void test_get_records_from_database() throws Exception {
        List<Cost> costList = TestUtils.createCountryCostRecords();
        Mockito.when(clearingCostService.findAll())
                .thenReturn(costList);

        this.mvc.perform(get("/cost-clearance/get-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].cost", is(costList.get(0).getCost())))
                .andExpect(jsonPath("$[0].country", is(costList.get(0).getCountry())))
                .andExpect(jsonPath("$[1].cost", is(costList.get(1).getCost())))
                .andExpect(jsonPath("$[1].country", is(costList.get(1).getCountry())))
                .andExpect(jsonPath("$[2].cost", is(costList.get(2).getCost())))
                .andExpect(jsonPath("$[2].country", is(costList.get(2).getCountry())));
    }

    @Test
    void test_get_record_from_database_by_id() throws Exception {
        CostDTO cost = TestUtils.createCountryCostRecord();
        Mockito.when(clearingCostService.findByCountry(GR))
                .thenReturn(cost);

        this.mvc.perform(get("/cost-clearance/get-by-country/GR"))
                .andExpect(status().isOk());
    }

    @Test
    void test_insert_country_and_cost() throws Exception {
        Mockito.when(clearingCostService.save(GR, 14))
                .thenReturn(TestUtils.createCountryCostRecord());

        this.mvc.perform(post("/cost-clearance/GR/15"))
                .andExpect(status().isOk());

    }

    @Test
    void test_update_record_by_country() throws Exception {
        Mockito.when(clearingCostService.updateCostByCountry(GR, 20))
                .thenReturn(1);

        this.mvc.perform(put("/cost-clearance/GR/20"))
                .andExpect(status().isOk());
    }

    @Test
    void test_delete_record_from_database() throws Exception {
        this.mvc.perform(delete("/cost-clearance/GR"))
                .andExpect(status().isOk());

        verify(clearingCostService).deleteByCountry(GR);
    }

    @Test
    public void test_get_card_cost_from_bin_list() throws Exception {
        CostRedisDTO costRedisEntity = TestUtils.createRedisCountryCost();
        Mockito.when(clearingCostService.process(Mockito.anyString()))
                .thenReturn(costRedisEntity);

        this.mvc.perform(get("/cost-clearance/516732"))
                .andExpect(status().isOk());
    }
}
