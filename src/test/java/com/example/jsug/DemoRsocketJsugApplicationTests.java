package com.example.jsug;

import org.junit.jupiter.api.Test;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestConstructor;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(properties = "spring.rsocket.server.port=0")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DemoRsocketJsugApplicationTests {

    private final RSocketRequester requester;

    DemoRsocketJsugApplicationTests(RSocketRequester.Builder builder, @LocalRSocketServerPort int port) {
        this.requester = builder.connectTcp("localhost", port).block();
    }

    @Test
    void hello() {
        final Mono<String> response = this.requester.route("hello")
            .data("JSUG")
            .retrieveMono(String.class);

        StepVerifier.create(response)
            .expectNext("Hello JSUG!")
            .expectComplete()
            .verify();
    }

}
