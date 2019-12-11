package com.example.jsug.duplex;

import com.example.jsug.vanilla.HelloHandler;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;

public class DuplexVanillaClient {

    public static void main(String[] args) throws Exception {
        final RSocket rsocket = RSocketFactory.connect()
            .acceptor(new HelloHandler())
            .transport(TcpClientTransport.create(7000))
            .start()
            .block();

        rsocket
            .requestResponse(DefaultPayload.create("Duplex Client"))
            .doOnNext(response -> System.out.println(">> " + response.getDataUtf8()))
            .doOnError(Throwable::printStackTrace)
            .subscribe();

        System.in.read();
        System.out.println("👋");
    }
}
