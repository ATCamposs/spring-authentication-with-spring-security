package com.url.shortener;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortenerApplicationTests {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void load() throws JSONException {
		var response = restTemplate.getForEntity(
				"/",
				String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getContentType().toString()).isEqualTo("application/json");
		assertThat(response.getBody()).isEqualTo("Hello World");
	}

}