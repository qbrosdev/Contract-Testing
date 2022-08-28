package com.qbros.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "PersonsProvider", port = "8081", pactVersion = PactSpecVersion.V3)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "producerRootURL:http://localhost:8081",
        classes = {ApiGateway.class, RestTemplate.class})
public class PersonResourceContractIntegrationTest {

    @Autowired
    ApiGateway apiGateway;

    @BeforeEach
    public void setUp(MockServer mockServer) {
        assertThat(mockServer).isNotNull();
        assertThat(mockServer.getUrl()).startsWith("http://");
    }

    @Pact(consumer = "PersonsConsumer")
    RequestResponsePact givenPersonsExistFetchShouldReturnSuccess(PactDslWithProvider builder) {
        return builder
                .given("Persons exist")
                .uponReceiving("retrieving all the persons data")
                .path("/persons")
                .method("GET")
                .willRespondWith()
                .headers(Map.of("Content-Type", "application/json"))
                .status(200)
                .body("{\n" +
                        "\t\"name\": \"alex\"\n" +
                        "}")
                .toPact();
    }

    @Pact(consumer = "PersonsConsumer")
    RequestResponsePact givenNoPersonsExistShouldReturnNotFound(PactDslWithProvider builder) {
        return builder
                .given("No Persons exist")
                .uponReceiving("retrieving no persons exist")
                .path("/persons")
                .method("GET")
                .willRespondWith()
                .headers(Map.of("Content-Type", "application/json"))
                .status(404)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "givenPersonsExistFetchShouldReturnSuccess")
    void givenListPersonShouldReturnOptionalPerson() {

        //when
        Optional<PersonSimple> response = apiGateway.fetchAllPerson();

        // then
        assertThat(response).isEqualTo(Optional.of(new PersonSimple("alex")));
    }

    @Test
    @PactTestFor(pactMethod = "givenNoPersonsExistShouldReturnNotFound")
    void givenNotFoundShouldReturnOptionalEmpty() {

        //when
        Optional<PersonSimple> response = apiGateway.fetchAllPerson();

        // then
        assertThat(response).isEqualTo(Optional.empty());
    }

}
