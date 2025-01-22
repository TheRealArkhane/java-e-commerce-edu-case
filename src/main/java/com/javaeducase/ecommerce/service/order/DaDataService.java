package com.javaeducase.ecommerce.service.order;

import com.javaeducase.ecommerce.client.DaDataClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DaDataService {

    private final DaDataClient daDataClient;

    public String validateAddress(String address) {
        log.info("Validating address with DaData service...");
        return daDataClient.getCleanAddressList(address).get(0).toString();
        }
}


