package com.portafolio.spotifyweb.config.componets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
@Slf4j
public class ServerInfoLogger {

    @EventListener(ApplicationReadyEvent.class)
    public void logServerInfo() {
        try {
            String localIP = InetAddress.getLocalHost().getHostAddress();
            String hostName = InetAddress.getLocalHost().getHostName();

            log.info("=================================");
            log.info("🚀 SERVER STARTED SUCCESSFULLY");
            log.info("📍 Local IP: {}", localIP);
            log.info("🏠 Host Name: {}", hostName);
            log.info("🔗 Base URL: http://{}:9000", localIP);
            log.info("📋 API Endpoints: http://{}:9000/v1/api/", localIP);
            log.info("=================================");

        } catch (Exception e) {
            log.error("Could not determine server IP", e);
        }
    }
}