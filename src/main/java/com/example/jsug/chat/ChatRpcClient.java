package com.example.jsug.chat;

import com.google.protobuf.Empty;
import com.salesforce.servicelibs.ChatClient;
import com.salesforce.servicelibs.ChatMessage;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

public class ChatRpcClient {

    public static void main(String[] args) throws Exception {
        final RSocket rsocket = RSocketFactory.connect()
            .transport(WebsocketClientTransport.create(7000))
            .start()
            .block();

        final ChatClient chat = new ChatClient(rsocket);

        String author = UUID.randomUUID().toString();

        chat.getMessages(Empty.getDefaultInstance())
            .filter(x -> !x.getAuthor().equals(author))
            .doOnNext(x -> System.out.printf("%s > %s%n", x.getAuthor(), x.getMessage()))
            .subscribe();


        Flux.interval(Duration.ofSeconds(1))
            .flatMap(i -> chat.postMessage(ChatMessage.newBuilder()
                .setAuthor(author)
                .setMessage("Hi " + i)
                .build()))
            .subscribe();

        System.in.read();
        chat.postMessage(ChatMessage.newBuilder()
            .setAuthor(author)
            .setMessage("👋").build()).block();
        System.out.println("👋");
    }
}
