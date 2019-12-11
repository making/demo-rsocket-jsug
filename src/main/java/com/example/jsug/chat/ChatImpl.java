package com.example.jsug.chat;

import com.google.protobuf.Empty;
import com.salesforce.servicelibs.Chat;
import com.salesforce.servicelibs.ChatMessage;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * https://github.com/salesforce/reactive-grpc/blob/master/demos/reactive-grpc-chat/reactor-chat/ReactorChat-Server/src/main/java/com/salesforce/servicelibs/ChatImpl.java
 */
public class ChatImpl implements Chat {

    private final Logger logger = LoggerFactory.getLogger(ChatImpl.class);

    private final EmitterProcessor<ChatMessage> broadcast = EmitterProcessor.create();

    @Override
    public Mono<Empty> postMessage(ChatMessage message, ByteBuf metadata) {
        logger.info(message.getAuthor() + ": " + message.getMessage());
        this.broadcast.onNext(message);
        return Mono.just(Empty.getDefaultInstance());
    }

    @Override
    public Flux<ChatMessage> getMessages(Empty message, ByteBuf metadata) {
        return this.broadcast;
    }
}
