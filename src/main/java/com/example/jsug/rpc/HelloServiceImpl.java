package com.example.jsug.rpc;

import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HelloServiceImpl implements HelloService {

    @Override
    public Mono<HelloResponse> sayHello(HelloRequest message, ByteBuf metadata) {
        final HelloResponse response = HelloResponse.newBuilder()
            .setReply(String.format("Hello %s!", message.getGreeting()))
            .build();
        return Mono.just(response);
    }

    @Override
    public Flux<HelloResponse> lotsOfReplies(HelloRequest message, ByteBuf metadata) {
        return Flux.range(0, 100000)
            .map(i -> HelloResponse.newBuilder()
                .setReply(String.format("[%05d] Hello %s!", i, message.getGreeting()))
                .build());
    }
}
