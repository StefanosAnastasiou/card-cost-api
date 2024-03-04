package com.etraveli.cardcostapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankDTO {
    private String name;
    private String website;
    private String phone;
}
