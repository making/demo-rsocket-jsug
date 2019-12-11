package com.example.jsug.duplex;

import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;

public class DuplexVanillaServer {

    public static void main(String[] args) throws Exception {
        RSocketFactory.receive()
            .acceptor(new DuplexHelloHandler())
            .transport(TcpServerTransport.create(7000))
            .start()
            .subscribe();

        System.in.read();
        System.out.println("👋");
    }
}
