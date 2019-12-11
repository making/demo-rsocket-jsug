package com.example.jsug.vanilla;

import io.rsocket.AbstractRSocket;
import io.rsocket.ConnectionSetupPayload;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HelloHandler implements SocketAcceptor {

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
        return Mono.just(new AbstractRSocket() {

            @Override
            public Mono<Payload> requestResponse(Payload payload) {
                final String response = String.format("Hello %s!", payload.getDataUtf8());
                return Mono.just(DefaultPayload.create(response));
            }

            @Override
            public Flux<Payload> requestStream(Payload payload) {
                return Flux.range(0, 100000)
                    .map(i -> {
                        final String response = String.format("[%05d] Hello %s!", i, payload.getDataUtf8());
                        return DefaultPayload.create(response);
                    });
            }
        });
    }
}
