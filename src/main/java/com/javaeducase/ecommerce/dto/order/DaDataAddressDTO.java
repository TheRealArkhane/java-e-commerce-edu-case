package com.javaeducase.ecommerce.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DaDataAddressDTO {
    @JsonProperty("region_with_type")
    private String regionWithType;

    @JsonProperty("city_with_type")
    private String cityWithType;

    @JsonProperty("street_with_type")
    private String streetWithType;

    @JsonProperty("house")
    private String house;

    @JsonProperty("house_type")
    private String houseType;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (regionWithType != null) {
            result.append(regionWithType);
        }
        if (cityWithType != null) {
            if (!result.isEmpty()) result.append(", ");
            result.append(cityWithType);
        }
        if (streetWithType != null) {
            if (!result.isEmpty()) result.append(", ");
            result.append(streetWithType);
        }
        if (houseType != null) {
            if (!result.isEmpty()) result.append(", ");
            result.append(houseType);
        }
        if (house != null) {
            if (!result.isEmpty()) result.append(" ");
            result.append(house);
        }
        return result.toString();
    }
}
