package com.portafolio.spotifyweb.config;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import java.time.LocalDateTime;


@Service
@Getter
@Slf4j
public class CredentialsSyncSpotify {

    @Autowired
    private SpotifyApiFactory apiFactory;

    private SpotifyApi spotifyApi;
    private ClientCredentialsRequest clientCredentialsRequest;
    private LocalDateTime tokenExpiry; // Agregar esto

    @PostConstruct
    public void init() {
        this.spotifyApi = apiFactory.createClientCredentialsApi();
        this.clientCredentialsRequest = spotifyApi.clientCredentials().build();
    }

    public ClientCredentials getClientCredentials() {
        // Auto-refresh si está expirado
        if (isTokenExpired()) {
            return refreshToken();
        }

        // Si no hay token, obtener uno nuevo
        if (spotifyApi.getAccessToken() == null) {
            return refreshToken();
        }

        return null; // Token aún válido
    }

    private boolean isTokenExpired() {
        return tokenExpiry == null ||
                LocalDateTime.now().isAfter(tokenExpiry.minusMinutes(5));
    }

    private ClientCredentials refreshToken() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            // Calcular cuándo expira
            tokenExpiry = LocalDateTime.now().plusSeconds(clientCredentials.getExpiresIn());

            log.info("Token refreshed - Expires in: {} seconds", clientCredentials.getExpiresIn());
            return clientCredentials;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error refreshing token: {}", e.getMessage());
            throw new RuntimeException("Failed to refresh token", e);
        }
    }
}