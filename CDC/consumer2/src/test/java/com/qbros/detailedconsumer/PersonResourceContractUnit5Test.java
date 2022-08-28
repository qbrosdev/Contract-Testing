package com.qbros.detailedconsumer;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


/*
 * PACT JVM Junit provides 3 ways to write the pact test file at consumer side, the Basic Junit, the Junit Rule and Junit DSL.
 * https://github.com/Mikuu/Pact-JVM-Example#21-create-test-cases
 *
 * Comparing with Basic Junit and Junit Rule usage, the DSL provides the ability to create multiple Pact files in one test class.
 */
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "PersonsProvider", port = "8080", pactVersion = PactSpecVersion.V3)
public class PersonResourceContractUnit5Test {

    @BeforeEach
    public void setUp(MockServer mockServer) {
        assertThat(mockServer).isNotNull();
        assertThat(mockServer.getUrl()).startsWith("http://");
    }

    @Pact(consumer = "PersonsDetailedConsumer")
    RequestResponsePact givenPersonsExistFetchShouldReturnHttpSuccess(PactDslWithProvider builder) {
        return builder
                .given("Persons exist")
                .uponReceiving("retrieving all the persons data should return http 200")
                .path("/persons")
                .method("GET")
                .willRespondWith()
                .headers(Map.of("Content-Type", "application/json"))
                .status(200)
                .body("{\n" +
                        "\t\"name\": \"alex\", \n" +
                        "\t\"age\": 123, \n" +
                        "\t\"salary\": 45 \n" +
                        "}")
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "givenPersonsExistFetchShouldReturnHttpSuccess")
    void givenListPersonShouldReturnOptionalPerson(MockServer mockServer) {

        RestTemplate restTemplate = new RestTemplate();

        //when
        ResponseEntity<PersonDetailed> response = restTemplate.getForEntity(mockServer.getUrl() + "/persons", PersonDetailed.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
