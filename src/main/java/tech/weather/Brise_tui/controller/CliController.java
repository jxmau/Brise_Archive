package tech.weather.Brise_tui.controller;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import tech.weather.Brise_tui.apps.air.AirPollutionParser;
import tech.weather.Brise_tui.apps.help.CliHelpService;
import tech.weather.Brise_tui.apps.hello.HelloParser;
import tech.weather.Brise_tui.apps.weather.now.WeatherNowParser;
import tech.weather.Brise_tui.settings.Settings;

@ShellComponent
public class CliController {

    private final WeatherNowParser weatherNowParser;
    private final CliHelpService cliHelpService;
    private final HelloParser helloParser;
    private final AirPollutionParser airPollutionParser;
    private final Settings settings;

    public CliController(WeatherNowParser weatherNowParser, CliHelpService cliHelpService, HelloParser helloParser, AirPollutionParser airPollutionParser, Settings settings) {
        this.weatherNowParser = weatherNowParser;
        this.cliHelpService = cliHelpService;
        this.helloParser = helloParser;
        this.airPollutionParser = airPollutionParser;
        this.settings = settings;
    }

    // Invoke Hello app
    @ShellMethod("Fetch main weather informations from a major city around the world.")
    public String hello(String city,
                        @ShellOption(defaultValue = "N/A") String country,
                        @ShellOption(defaultValue = "N/A") String state){
        return helloParser.fetchHelloInformations(city, country, state);
    }

    // Invoke weather app
    @ShellMethod("Feath current weather informations from a city\n-s to save city informations.")
    public String now(@ShellOption(defaultValue = "N/A") String city,
                        @ShellOption(defaultValue = "N/A") String country,
                        @ShellOption(defaultValue = "N/A") String state,
                        @ShellOption(defaultValue = "N/A") String command){
        if (city.equals("N/A")){
            return weatherNowParser.fetchWeatherInfosForSavedCity();
        } else {
            return weatherNowParser.fetchWeatherInformations(city, country, state, command);
        }
    }

    // Invoke Air Pollution app
    @ShellMethod("Fetch air quality informations from a major city around the world.\n-s to save city informations.")
    public String air(@ShellOption(defaultValue = "N/A") String city,
                      @ShellOption(defaultValue = "N/A") String country,
                      @ShellOption(defaultValue = "N/A") String state,
                      @ShellOption(defaultValue = "N/A") String command){
        return switch (city) {
            case "-names" -> cliHelpService.airNames();
            case "-limits" -> cliHelpService.airLimits();
            case "N/A" -> airPollutionParser.fetchAirPollutionInfosForSavedCity();
            default -> airPollutionParser.fetchAirPollutionInformations(city, country, state, command);
        };
    }

    @ShellMethod("Get help")
    public String help(){
        return cliHelpService.help();
    }

    @ShellMethod("Modify your AppId Key")
    public String key(String key){
        return settings.modifyAppId(key) + "Please, restart the application.";
    }

    @ShellMethod("Exit the shell")
    public void exit(){
        System.exit(0);
    }


}
