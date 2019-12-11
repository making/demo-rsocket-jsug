package com.example.jsug.vanilla;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import io.rsocket.util.DefaultPayload;

public class VanillaClient {

    public static void main(String[] args) throws Exception {
        final RSocket rsocket = RSocketFactory.connect()
            .transport(WebsocketClientTransport.create(7000))
            .start()
            .block();

        rsocket
            .requestResponse(DefaultPayload.create("RSocket Client"))
            .doOnNext(response -> System.out.println(">> " + response.getDataUtf8()))
            .doOnError(Throwable::printStackTrace)
            .subscribe();

        System.in.read();

        rsocket.requestStream(DefaultPayload.create("RSocket Client"))
            .doOnNext(response -> System.out.println(">> " + response.getDataUtf8()))
            .limitRate(4)
            .take(10)
            .subscribe();
        System.in.read();
        System.out.println("👋");
    }
}
