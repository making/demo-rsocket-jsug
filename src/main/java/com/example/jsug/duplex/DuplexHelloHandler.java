package com.example.jsug.duplex;

import io.rsocket.AbstractRSocket;
import io.rsocket.ConnectionSetupPayload;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Mono;

public class DuplexHelloHandler implements SocketAcceptor {

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
        return Mono.just(new AbstractRSocket() {

            @Override
            public Mono<Payload> requestResponse(Payload payload) {
                // ***************
                sendingSocket.requestResponse(DefaultPayload.create("Duplex Server"))
                    .doOnNext(response -> System.out.println(">> " + response.getDataUtf8()))
                    .doOnError(Throwable::printStackTrace)
                    .subscribe();
                // ***************

                final String response = String.format("Hello %s!", payload.getDataUtf8());
                return Mono.just(DefaultPayload.create(response));
            }
        });
    }
}
