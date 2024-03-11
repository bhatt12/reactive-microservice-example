package com.example.unit;

import com.example.web.handler.ReactiveController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@WebFluxTest(controllers = ReactiveController.class)
@AutoConfigureWebTestClient
public class ReactiveControllerTests {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testMonoValueController(){
        webTestClient
                .get()
                .uri("/value")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .equals("TestValue");
    }

    @Test
    void testFluxValuesController(){
        var flux = webTestClient
                .get()
                .uri("/values")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier.create(flux)
                .expectNext("ABC")
                .verifyComplete();
    }

    @Test
    void testUntFluxValuesController(){
        webTestClient
                .get()
                .uri("/int-values")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> {
                    var responseBody = listEntityExchangeResult.getResponseBody();
                    assert (responseBody.size() == 3);
                });

    }

    @Test
    void testInfiniteFLux(){
        var flux = webTestClient
                .get()
                .uri("/infinite/values")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(flux)
                .expectNext(0L, 1L, 2L, 3L)
                .thenCancel()
                .verify();
    }
}
