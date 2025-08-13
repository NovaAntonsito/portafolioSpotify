package com.portafolio.spotifyweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.michaelthelin.spotify.SpotifyApi;

@Configuration
public class SpotifyWebConfig {

    @Autowired
    private CredentialsSyncSpotify credentialsSync;

    @Autowired
    private AuthorizationCodeService authorizationCodeService;

    @Bean("publicSpotifyApi")
    public SpotifyApi userSpotifyApi(CredentialsSyncSpotify credentialsSyncSpotify) {
        // Para búsquedas públicas (sin login)
        credentialsSyncSpotify.getClientCredentials();
        return credentialsSync.getSpotifyApi();
    }
    @Bean("privateSpotifyApi")
    public SpotifyApi publicSpotifyApi () {
        // Para operaciones del usuario (con login)
        return authorizationCodeService.getSpotifyApi();
    }
}