package com.ira.feigntest;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class FeigntestApplication implements CommandLineRunner{

	private static final Logger logger = LoggerFactory.getLogger(FeigntestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FeigntestApplication.class, args);
	}


	interface GitHub{
		@RequestLine("GET /repos/{owner}/{repo}/contributors")
		List<Contributor> contributors(@Param("owner") String owner, @Param("repo") String repo);
	}

	private static class Contributor {
		String login;
		int contributions;
	}

	@Override
	public void run(String... strings) throws Exception {
		GitHub gitHub = Feign.builder()
				.decoder(new GsonDecoder())
				.target(GitHub.class, "https://api.github.com");
		List<Contributor> contributors = gitHub.contributors("OpenFeign", "feign");
		for (Contributor contributor : contributors) {
			logger.info(contributor.login + " (" + contributor.contributions + ")");
		}
	}
}
