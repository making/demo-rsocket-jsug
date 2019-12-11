package com.example.jsug.rpc;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.WebsocketClientTransport;

public class RpcClient {

    public static void main(String[] args) throws Exception {
        final RSocket rsocket = RSocketFactory.connect()
            .transport(WebsocketClientTransport.create(7000))
            .start()
            .block();

        final HelloServiceClient helloService = new HelloServiceClient(rsocket);

        helloService
            .sayHello(HelloRequest.newBuilder().setGreeting("RPC Client").build())
            .doOnNext(response -> System.out.println(">> " + response.getReply()))
            .doOnError(Throwable::printStackTrace)
            .subscribe();

        System.in.read();

        helloService.lotsOfReplies(HelloRequest.newBuilder().setGreeting("RPC Client").build())
            .doOnNext(response -> System.out.println(">> " + response.getReply()))
            .limitRate(4)
            .take(10)
            .subscribe();
        System.in.read();
        System.out.println("👋");
    }
}
