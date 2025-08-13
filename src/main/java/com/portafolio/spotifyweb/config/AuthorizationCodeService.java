package com.portafolio.spotifyweb.config;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;

@Service
@Getter
public class AuthorizationCodeService {

    @Autowired
    private SpotifyApiFactory apiFactory;

    private SpotifyApi spotifyApi;
    private AuthorizationCodeUriRequest authorizationCodeUriRequest;

    @PostConstruct
    public void init() {
        this.spotifyApi = apiFactory.createAuthorizationCodeApi();
        this.authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-private,user-read-email,user-library-read,user-library-modify,playlist-read-private,playlist-modify-public,playlist-modify-private,user-read-playback-state,user-modify-playback-state,user-read-currently-playing")
                .show_dialog(true)
                .build();
    }

    public URI getAuthorizationUri() {
        final URI uri = authorizationCodeUriRequest.execute();
        return uri;
    }
}