package com.etraveli.cardcostapi.utils;

import com.etraveli.cardcostapi.entity.CostRedisEntity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CountryCostDeserializer extends StdDeserializer<CostRedisEntity> {

    public CountryCostDeserializer() {
        this(null);
    }

    public CountryCostDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CostRedisEntity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException,
            JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String country =  node.get("country").get("alpha2").asText();

        if(country.equals("US")){
            return CostRedisEntity.builder()
                    .country("US")
                    .cost(5.00)
                    .build();
        } else if (country.equals("GR")) {
            return CostRedisEntity.builder()
                    .country("GR")
                    .cost(15.00)
                    .build();
        }

        return CostRedisEntity.builder()
                .country("Others")
                .cost(10.00)
                .build();
    }
}
