package com.example.jsug.spring;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    @MessageMapping("hello")
    public Mono<String> hello(Mono<String> request) {
        return request.map(s -> String.format("Hello %s!", s));
    }

    @MessageMapping("hello.many")
    public Flux<String> helloMany(Mono<String> request) {
        return request.flatMapMany(s ->
            Flux.range(0, 100000)
                .map(i -> String.format("[%05d] Hello %s!", i, s)));
    }
}
