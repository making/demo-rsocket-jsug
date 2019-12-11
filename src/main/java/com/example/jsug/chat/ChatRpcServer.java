package com.example.jsug.chat;

import com.salesforce.servicelibs.Chat;
import com.salesforce.servicelibs.ChatServer;
import io.rsocket.RSocketFactory;
import io.rsocket.rpc.rsocket.RequestHandlingRSocket;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class ChatRpcServer {

    public static void main(String[] args) throws Exception {
        final Chat chat = new ChatImpl();
        final ChatServer chatServer = new ChatServer(chat, Optional.empty(), Optional.empty());
        RSocketFactory.receive()
            .acceptor((setup, sendingSocket) -> Mono.just(new RequestHandlingRSocket(chatServer)))
            .transport(WebsocketServerTransport.create(7000))
            .start()
            .subscribe();

        System.in.read();
        System.out.println("👋");
    }
}
