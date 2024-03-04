package com.etraveli.cardcostapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryDTO {
    private String numeric;
    private String alpha2;
    private String name;
    private String emoji;
    private String currency;
    private Integer latitude;
    private Integer longitude;
}
