package com.qbros.detailedconsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class ApiGateway {

    private final RestTemplate restTemplate;
    private final String url;

    public ApiGateway(@Autowired RestTemplate restTemplate, @Value("${producerRootURL:http://localhost:8080/}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public Optional<PersonDetailed> fetchAllPerson() {
        try {
            PersonDetailed result = restTemplate.getForEntity(url + "/persons", PersonDetailed.class).getBody();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("Something nasty happened", e);
        }
        return Optional.empty();
    }
}
