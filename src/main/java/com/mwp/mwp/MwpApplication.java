package com.mwp.mwp;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MwpApplication {

    @Autowired
    private SocketIOServer socketIOServer;

    public static void main(String[] args) {
        SpringApplication.run(MwpApplication.class, args);
    }

    @PostConstruct
    public void init() {
        socketIOServer.start();
    }

    @PreDestroy
    public void destroy() {
        socketIOServer.stop();
    }
}
