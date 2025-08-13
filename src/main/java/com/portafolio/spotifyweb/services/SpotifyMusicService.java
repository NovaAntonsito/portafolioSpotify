package com.portafolio.spotifyweb.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.neovisionaries.i18n.CountryCode;
import com.portafolio.spotifyweb.services.utils.SpotifyPreviewURLService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.hc.core5.http.ParseException;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpotifyMusicService {


    private SpotifyApi publicApi; // Para búsquedas

    private SpotifyApi userApi; // Para datos del usuario

    private String miPlaylistID; // ID de mi playlist (Un poco de todo)

    private SpotifyPreviewURLService  spotifyPreviewURLService;



    public SpotifyMusicService(
            @Qualifier("privateSpotifyApi") SpotifyApi publicApi,
            @Qualifier("publicSpotifyApi") SpotifyApi userApi,
            @Value("${playlist.id}") String miPlaylistID,
            SpotifyPreviewURLService spotifyPreviewURLService) {
        this.publicApi = publicApi;
        this.userApi = userApi;
        this.miPlaylistID = miPlaylistID;
        this.spotifyPreviewURLService = spotifyPreviewURLService;
    }

    public List<Map<String, String>> searchTracks(String name, String artist) throws IOException, ParseException, SpotifyWebApiException {
        try {
            String query = String.format("track:\"%s\" artist:\"%s\"", name, artist);
            SearchTracksRequest searchRequest = publicApi.searchTracks(query)
                    .limit(20)
                    .offset(0)
                    .includeExternal("audio")
                    .build();

            Paging<Track> trackPaging = searchRequest.execute();

            List<Track> tracks = List.of(trackPaging.getItems());

            List<Map<String, String>> jsonSpotify = new ArrayList<>();

            for (Track track : tracks) {
                Map<String, String> spotifyMap = new HashMap<>();
                spotifyMap.put("id", track.getId());
                spotifyMap.put("nombre", track.getName());
                spotifyMap.put("artista", track.getArtists()[0].getName());
                spotifyMap.put("previewURL", spotifyPreviewURLService.getPreviewURL(track.getId()));
                spotifyMap.put("SpotifyURL", track.getExternalUrls().get("spotify"));
                jsonSpotify.add(spotifyMap);
            }

            return jsonSpotify;


        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error en búsqueda: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            List<Map<String, String>> errorList = new ArrayList<>();
            errorList.add(errorMap);
            return errorList;
        }
    }

    public List<Map<String, String>> getPlaylistTracks() {
        try {
            GetPlaylistsItemsRequest playlistItemsRequest = userApi
                    .getPlaylistsItems(miPlaylistID)
                    .limit(50)                    // Máximo 50 tracks por petición
                    .offset(0)                    // Para paginación
                    .additionalTypes("track")     // Solo tracks (no episodes)
                    .build();

            Paging<PlaylistTrack> playlistTracks = playlistItemsRequest.execute();

            List<Map<String, String>> trackList = new ArrayList<>();

            for (PlaylistTrack playlistTrack : playlistTracks.getItems()) {
                if (playlistTrack.getTrack() instanceof Track) {
                    Track track = (Track) playlistTrack.getTrack();

                    Map<String, String> trackMap = new HashMap<>();
                    trackMap.put("id", track.getId());
                    trackMap.put("name", track.getName());
                    trackMap.put("artist", track.getArtists()[0].getName());
                    trackMap.put("album", track.getAlbum().getName());
                    trackMap.put("previewUrl", spotifyPreviewURLService
                            .getPreviewURL(track.getId()));
                    trackMap.put("spotifyUrl", track.getExternalUrls().get("spotify"));


                    trackList.add(trackMap);
                }
            }

            return trackList;

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error obteniendo tracks de playlist: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}