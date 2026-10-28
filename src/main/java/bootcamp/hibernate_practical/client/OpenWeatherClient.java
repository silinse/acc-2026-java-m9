package bootcamp.hibernate_practical.client;

import bootcamp.hibernate_practical.exception.WeatherClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;

@Component
public class OpenWeatherClient {

    private final String apiKey;
    private final RestClient restClient;

    public OpenWeatherClient(
            RestClient.Builder builder,
            @Value("${openweather.api.key}") String apiKey,
            @Value("${openweather.api.base-url}") String baseUrl) {

        this.apiKey = apiKey;

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofSeconds(5));
        factory.setConnectTimeout(Duration.ofSeconds(5));

        this.restClient = builder
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();
    }

    public String getWeather(String city) {
        if (apiKey.isBlank()) {
            throw new WeatherClientException("OpenWeather API key is not configured");
        }

        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/data/2.5/weather")
                            .queryParam("q", city)
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        if (res.getStatusCode().value() == 401) {
                            throw new WeatherClientException("Not authorized for external API service");
                        } else if (res.getStatusCode().value() == 404) {
                            throw new WeatherClientException("City \"" + city + "\" not found");
                        } else if (res.getStatusCode().value() == 429) {
                            throw new WeatherClientException("Too many requests for external API service");
                        }
                        throw new WeatherClientException("External API client error: " + res.getStatusCode());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new WeatherClientException("External API server error: " + res.getStatusCode());
                    })
                    .body(String.class);

        } catch (ResourceAccessException e) {
            throw new WeatherClientException("External API connection failed or timed out", e);
        } catch (RestClientException e) {
            throw new WeatherClientException("External API returned an invalid response", e);
        }
    }
}
