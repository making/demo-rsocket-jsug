package com.example.jsug.vanilla;

import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.WebsocketServerTransport;

public class VanillaServer {

    public static void main(String[] args) throws Exception {
        RSocketFactory.receive()
            .acceptor(new HelloHandler())
            .transport(WebsocketServerTransport.create(7000))
            .start()
            .subscribe();

        System.in.read();
        System.out.println("👋");
    }
}
