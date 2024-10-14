package com.javaeducase.ecommerce.service.order;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DaDataService {
    private static final String API_URL = "https://cleaner.dadata.ru/api/v1/clean/address";
    private static final String TOKEN = "47fef20659bbdeb536f5300dafbeddb5792e7076";
    private static final String SECRET = "00a2c992072451f940891e392d9c1c9e4c432f21";

    public String validateAddress(String address) {
        HttpResponse<JsonNode> response = Unirest.post(API_URL)
                .header("Authorization", "Token " + TOKEN)
                .header("X-Secret", SECRET)
                .header("Content-Type", "application/json")
                .body("[\"" + address + "\"]")
                .asJson();

        JSONArray jsonArray = response.getBody().getArray();
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        String regionWithType = jsonObject.getString("region_with_type");
        String cityWithType = jsonObject.getString("city_with_type");
        String streetWithType = jsonObject.getString("street_with_type");
        String house = jsonObject.getString("house");
        String houseType = jsonObject.getString("house_type");
        return regionWithType + ", " + cityWithType + ", " + streetWithType + ", " + houseType + " " + house;
    }
}
