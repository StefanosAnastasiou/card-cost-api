package com.etraveli.cardcostapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CostRedisDTO implements Serializable {
    private static final long serialVersionUID = -4559821579447393188L;
    @JsonProperty("country")
    private String country;
    @JsonProperty("cost")
    private Double cost;
}