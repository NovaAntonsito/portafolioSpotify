package com.portafolio.spotifyweb.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

import java.net.URI;

@Service
public class SpotifyApiFactory {

    @Getter(AccessLevel.NONE)
    @Value("${spotify.client.id}")
    private String clientID;
    @Getter(AccessLevel.NONE)
    @Value("${spotify.client.secret}")
    private String clientSecret;
    @Value("${spotify.success.uri}")
    private String redirectUri;

    public SpotifyApi createClientCredentialsApi() {
        return new SpotifyApi.Builder()
                .setClientId(clientID)
                .setClientSecret(clientSecret)
                .build();
    }

    public SpotifyApi createAuthorizationCodeApi() {
        URI redirectUriParsed = SpotifyHttpManager.makeUri(redirectUri);
        return new SpotifyApi.Builder()
                .setClientId(clientID)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUriParsed)
                .build();
    }
}