package com.portafolio.spotifyweb.services.utils;

import com.jayway.jsonpath.JsonPath;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SpotifyPreviewURLService {

    private OkHttpClient httpClient;

    @PostConstruct
    public void init() {
        this.httpClient = new OkHttpClient();
    }

    public String getPreviewURL(String spotifyID) throws IOException {
        String embedURL = "https://open.spotify.com/embed/track/" + spotifyID;

        Request request = new Request.Builder()
                .url(embedURL)
                .build();

        Response response = httpClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            return null;
        }

        // response.body().string() automatically closes the response body
        String html = response.body().string();
        Document document = Jsoup.parse(html);
        Elements scriptElements = document.getElementsByTag("script");

        for (Element element : scriptElements) {
            String scriptContent = element.html();
            if (!scriptContent.isEmpty()) {
                String previewUrl = findPreviewUrlWithJsonPath(scriptContent, "audioPreview");
                if (previewUrl != null) {
                    return previewUrl;
                }
            }
        }

        return null;
    }

    private String findPreviewUrlWithJsonPath(String jsonString, String targetNode) {
        try {
            String query = "$.." + targetNode + ".url";
            Object result = JsonPath.read(jsonString, query);

            if (result instanceof List) {
                List<?> resultList = (List<?>) result;
                if (!resultList.isEmpty()) {
                    return resultList.get(0).toString();
                }
            } else if (result instanceof String) {
                return (String) result;
            }

        } catch (Exception e) {
            log.error("Error parsing JSON for preview URL: {}", e.getMessage());
        }

        return null;
    }
}