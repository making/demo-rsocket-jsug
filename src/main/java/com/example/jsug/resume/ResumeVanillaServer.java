package com.example.jsug.resume;

import com.example.jsug.vanilla.HelloHandler;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;

public class ResumeVanillaServer {

    public static void main(String[] args) throws Exception {
        RSocketFactory.receive()
            // ***************
            .resume()
            // ***************
            .acceptor(new HelloHandler())
            .transport(TcpServerTransport.create(7000))
            .start()
            .subscribe();

        System.in.read();
        System.out.println("👋");
    }
}
