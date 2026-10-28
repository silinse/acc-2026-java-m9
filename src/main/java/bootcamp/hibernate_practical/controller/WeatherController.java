package bootcamp.hibernate_practical.controller;

import bootcamp.hibernate_practical.client.OpenWeatherClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final OpenWeatherClient openWeatherClient;

    public WeatherController(OpenWeatherClient openWeatherClient) {
        this.openWeatherClient = openWeatherClient;
    }

    @GetMapping("/{city}")
    public String getWeather(@PathVariable String city) {
        return openWeatherClient.getWeather(city);
    }
}
