package tech.weather.Brise_tui.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import tech.weather.Brise_tui.application.AirPollutionApplication;
import tech.weather.Brise_tui.application.HelloApplication;
import tech.weather.Brise_tui.application.WeatherApplication;
import org.springframework.web.client.RestTemplate;
import tech.weather.Brise_tui.service.ressource.FetchGPSCoordinates;
import tech.weather.Brise_tui.settings.Settings;


import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private final HelloApplication helloApplication;
    private final WeatherApplication weatherApplication;
    private final AirPollutionApplication airPollutionApplication;
    private final RestTemplate restTemplate;
    private final FetchGPSCoordinates fetchGPSCoordinates;
    private final Settings settings;
    private final String appId;

    public WeatherService(Settings settings, HelloApplication helloApplication, WeatherApplication weatherApplication, AirPollutionApplication airPollutionApplication, RestTemplateBuilder restTemplateBuilder, FetchGPSCoordinates fetchGPSCoordinates) {
        this.helloApplication = helloApplication;
        this.weatherApplication = weatherApplication;
        this.airPollutionApplication = airPollutionApplication;
        this.restTemplate = restTemplateBuilder.build();
        this.fetchGPSCoordinates = fetchGPSCoordinates;
        this.settings = settings;
        this.appId = settings.GetAppId();
    }



    // Fetch Json For city
    public String fetchWeatherInformations(String city, String country, String state, String command) {
        if (country.equals("-s") || state.equals("-s")){
            command = "-s";
            country = "N/A";
            state = "N/A";
        }

        Map<String, Map<String, Object>> jsonResponse =
                restTemplate.getForObject(
                        urlAssemblerForWeather(city, country, state),
                        Map.class);

        if (command.equals("-s")) {
            Map<String, String> coordinates = fetchGPSCoordinates.fetchCoordinates(jsonResponse);
            settings.saveCoord(city, country, state, coordinates.get("latitude"), coordinates.get("longitude"));
        }

        return generateBulletin(city, country, jsonResponse);
    }


    //Fetch weather informations for saved city
    public String fetchWeatherInfosForSavedCity(){
        Map<String, String> coord = settings.getCoord();
        String city = coord.get("city");
        String state = coord.get("state");
        Map<String, Map<String, Object>> jsonResponse =
                restTemplate.getForObject(
                        urlAssemblerForWeather(city, coord.get("country"), state),
                        Map.class);


        return generateBulletin(city, state, jsonResponse);
    }

    // Generate bulletin
    public String generateBulletin(String city, String state, Map<String, Map<String, Object>> jsonResponse ){
        if (city != null) {
            if (!state.equals("N/A")) {
                return "\nWeather bulletin for " + city + ", " + state + "\n" + weatherApplication.generateInformations(jsonResponse);
            } else {
                return "\nWeather bulletin for " + city + "\n" + weatherApplication.generateInformations(jsonResponse);
            }
        } else {
            return "Sorry, the city couldn't be find!";
        }

    }


    private String urlAssemblerForWeather(String city, String country, String state){
        if (!country.equals("N/A") && state.equals("N/A")){

            return "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country
                    + "&appid=" + appId;
        } else if (!country.equals("N/A") && !state.equals("N/A")){

            return "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + state
                    + "," + country + "&appid=" + appId;
        } else {
            return "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + appId;
        }

    }



}
