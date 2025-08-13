package com.portafolio.spotifyweb.config.Cron;

import com.portafolio.spotifyweb.config.CredentialsSyncSpotify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@Slf4j
public class SpotifyTokenInterceptor {

    @Autowired
    private CredentialsSyncSpotify credentialsSync;

    private LocalDateTime lastRefresh;
    private final Duration TOKEN_DURATION = Duration.ofMinutes(55);

    public void ensureValidToken() {
        if (needsRefresh()) {
            refreshToken();
        }
    }

    private boolean needsRefresh() {
        return lastRefresh == null ||
                lastRefresh.plus(TOKEN_DURATION).isBefore(LocalDateTime.now());
    }

    private void refreshToken() {
        try {
            credentialsSync.getClientCredentials(); // Usa tu método existente
            lastRefresh = LocalDateTime.now();
            log.info("Token refreshed successfully via interceptor");
        } catch (Exception e) {
            log.error("Failed to refresh token via interceptor", e);
            throw new RuntimeException("Token refresh failed", e);
        }
    }
}