package com.etraveli.cardcostapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinListResponseDTO {
    private NumberDTO number;
    private String scheme;
    private String type;
    private String brand;
    private CountryDTO country;
    private BankDTO bank;
}
