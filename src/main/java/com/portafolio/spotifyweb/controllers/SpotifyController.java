package com.portafolio.spotifyweb.controllers;


import com.portafolio.spotifyweb.services.SpotifyMusicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/v1/api/music")
public class SpotifyController {

    @Autowired
    private SpotifyMusicService spotifyMusicService;

    @GetMapping("/getMylist")
    public ResponseEntity<List<Map<String,String>>> home() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(spotifyMusicService.getPlaylistTracks());
    }

    @GetMapping("/searchTrack")
    public ResponseEntity<List<Map<String,String>>> stack(@RequestParam String name, @RequestParam String artist) throws IOException, ParseException, SpotifyWebApiException {
        return ResponseEntity.status(HttpStatus.OK).body(spotifyMusicService.searchTracks(name, artist));
    }
}
