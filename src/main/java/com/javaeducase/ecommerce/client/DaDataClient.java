package com.javaeducase.ecommerce.client;

import com.javaeducase.ecommerce.dto.order.DaDataAddressDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${daDataClient.token}")
    private String token;

    @Value("${daDataClient.secret}")
    private String secret;

    private final RestTemplate restTemplate;

    public List<DaDataAddressDTO> getCleanAddressList(String address) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + token);
        headers.set("X-Secret", secret);
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
        return responseEntity.getBody();
    }
}

