package com.javaeducase.ecommerce.client;

import com.javaeducase.ecommerce.dto.order.DaDataAddressDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DaDataClient {

    private static final String API_URL = "https://cleaner.dadata.ru/api/v1/clean/address";
    private static final String TOKEN = "47fef20659bbdeb536f5300dafbeddb5792e7076";
    private static final String SECRET = "00a2c992072451f940891e392d9c1c9e4c432f21";

    private final RestTemplate restTemplate;

    public DaDataAddressDTO getCleanAddress(String address) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + TOKEN);
        headers.set("X-Secret", SECRET);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String jsonBody = "[\"" + address + "\"]";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<List<DaDataAddressDTO>> responseEntity = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
        if (responseEntity.getBody() == null) {
            throw new RuntimeException("Empty response from DaData API");
        }
        return responseEntity.getBody().get(0);
    }
}

