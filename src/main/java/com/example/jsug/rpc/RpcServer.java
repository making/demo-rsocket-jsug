package com.example.jsug.rpc;

import io.rsocket.RSocketFactory;
import io.rsocket.rpc.rsocket.RequestHandlingRSocket;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class RpcServer {

    public static void main(String[] args) throws Exception {
        final HelloService helloService = new HelloServiceImpl();
        final HelloServiceServer helloServiceServer = new HelloServiceServer(helloService, Optional.empty(), Optional.empty());
        RSocketFactory.receive()
            .acceptor((setup, sendingSocket) -> Mono.just(new RequestHandlingRSocket(helloServiceServer)))
            .transport(WebsocketServerTransport.create(7000))
            .start()
            .subscribe();

        System.in.read();
        System.out.println("👋");
    }
}
