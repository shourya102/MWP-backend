package com.mwp.mwp.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIoConfig {

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
        configuration.setHostname("localhost");
        configuration.setPort(1250);
        SocketConfig socketConfig = configuration.getSocketConfig();
        socketConfig.setReuseAddress(true);
        return new SocketIOServer(configuration);
    }
}
