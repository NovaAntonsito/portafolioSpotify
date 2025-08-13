package com.portafolio.spotifyweb.config.Cron;

import com.portafolio.spotifyweb.config.CredentialsSyncSpotify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class SpotifyTokenRefreshScheduler {

    @Autowired
    private CredentialsSyncSpotify credentialsSync;

    // Refresh cada 50 minutos (tokens duran 1 hora)
    @Scheduled(fixedRate = 50 * 60 * 1000)
    public void refreshToken() {
        try {
            credentialsSync.getClientCredentials();
            log.info("Token refreshed successfully");
        } catch (Exception e) {
            log.error("Failed to refresh token", e);
        }
    }
}