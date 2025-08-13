package com.portafolio.spotifyweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import se.michaelthelin.spotify.SpotifyApi;

@SpringBootApplication
@EnableScheduling
public class PortafolioSpotifyApplication{

	public static void main(String[] args) {
		SpringApplication.run(PortafolioSpotifyApplication.class, args);
	}

}
