package com.example.jsug;

import com.example.jsug.rpc.HelloService;
import com.example.jsug.rpc.HelloServiceImpl;
import com.example.jsug.rpc.HelloServiceServer;
import com.example.jsug.vanilla.HelloHandler;
import io.rsocket.rpc.rsocket.RequestHandlingRSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.rsocket.context.RSocketServerBootstrap;
import org.springframework.boot.rsocket.server.RSocketServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Mono;

import java.util.Optional;

@SpringBootApplication(scanBasePackages = "com.example.jsug.spring")
public class DemoRsocketJsugApplication {

    @Bean
    @Profile("raw")
    public RSocketServerBootstrap rawRSocket(RSocketServerFactory factory) {
        return new RSocketServerBootstrap(factory, new HelloHandler());
    }

    @Bean
    @Profile("rpc")
    public RSocketServerBootstrap rpc(RSocketServerFactory factory) {
        final HelloService helloService = new HelloServiceImpl();
        final HelloServiceServer serviceServer = new HelloServiceServer(helloService, Optional.empty(), Optional.empty());
        return new RSocketServerBootstrap(factory, (setup, sendingSocket) -> Mono.just(new RequestHandlingRSocket(serviceServer)));
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoRsocketJsugApplication.class, args);
    }

}
