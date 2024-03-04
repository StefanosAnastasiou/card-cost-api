package com.etraveli.cardcostapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CostDTO implements Serializable {
    private static final long serialVersionUID = -7423985094187713364L;
    @JsonProperty("country")
    private String country;
    @JsonProperty("cost")
    private Double cost;
}
