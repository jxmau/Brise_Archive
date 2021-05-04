package tech.weather.Brise_tui.apps.air;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.lang.Math;

@Component
public class AirPollutionAssembler {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public String generateInformations(Map<String, Map<String, Object>> jsonResponse) {
        List<Map<String, Object>> jsonResponseList = (List<Map<String, Object>>) jsonResponse.get("list");
        Map<String, Object> airQualityInfos = jsonResponseList.get(0);
        Map<String, Object> airPollutants = (Map<String, Object>) airQualityInfos.get("components");
        Map<String, Integer> main = (Map<String, Integer>) airQualityInfos.get("main");
        return airPollutantsInformations(airPollutants) + "\n > Air Quality : " + airQualityCondition(main.get("aqi")) + "\n"+
                "(type air -limits to know more about the colour code.)";

    }

    // Generate the pollutants bulletin
    // Variables are received as Double, except when equals as zero, then they are Integer.
    private String airPollutantsInformations(Map<String, Object> airPollutants) {
        Double co = Double.valueOf(airPollutants.get("co").toString());
        Double no = Double.valueOf(airPollutants.get("no").toString());
        Double no2 = Double.valueOf(airPollutants.get("no2").toString());
        Double o3 = Double.valueOf(airPollutants.get("o3").toString());
        Double so2 = Double.valueOf(airPollutants.get("so2").toString());
        Double nh3 = Double.valueOf(airPollutants.get("nh3").toString());
        Double pm2_5 = Double.valueOf(airPollutants.get("pm2_5").toString());
        Double pm10 = Double.valueOf(airPollutants.get("pm10").toString());
        return "\n > Molecules : \n" +
                "CO : " + getCO(co) + " μg/m3 | NO : " + no + " μg/m3\n" +
                "NO2 : " + getNO2(no2) + " μg/m3 | O3 : " + getO3(o3) + " μg/m3\n" +
                "SO2 : " + getSO2(so2) + " μg/m3 | NH3 : " + nh3 + " μg/m3\n" +
                " > Particulates : \n" +
                "PM2.5 : " + getPM2_5(pm2_5) + " μg/m3 | PM10 : " + getPM10(pm10) + " μg/m3\n";


    }

    // get Air Quality Condition
    private String airQualityCondition(Integer condition) {
        switch (condition) {
            case 1: {
                return "Good";
            }
            case 2: {
                return "Moderate to Good";
            }
            case 3: {
                return "Moderate";
            }
            case 4: {
                return "Bad";
            }
            case 5: {
                return "Dangerous";
            }
            default: {
                return "Informations not available at the moment.";
            }
        }

    }

    // Get CO
    private String getCO(Double co) {
        if (co >= 10310) {
            return ANSI_RED + co + ANSI_RESET;
        } else if (co >= 6874) {
            return ANSI_YELLOW + co + ANSI_RESET;
        } else {
            return ANSI_GREEN + co + ANSI_RESET;
        }
    }


    // Get NO2
    private String getNO2(Double no2) {
        if (no2 >= 40) {
            return ANSI_RED + no2 + ANSI_RESET;
        } else {
            return ANSI_GREEN + no2 + ANSI_RESET;
        }
    }

    // Get O3
    private String getO3(Double o3) {
        if (o3 >= 100) {
            return ANSI_RED + o3 + ANSI_RESET;
        } else {
            return ANSI_GREEN + o3 + ANSI_RESET;
        }
    }

    // Get SO2 
    private String getSO2(Double so2) {
        if (so2 >= 29) {
            return ANSI_RED + so2 + ANSI_RESET;
        } else {
            return ANSI_GREEN + so2 + ANSI_RESET;
        }
    }

    // Get PM2_5
    private String getPM2_5(Double pm2_5) {
        if (pm2_5 >= 25) {
            return ANSI_RED + pm2_5 + ANSI_RESET;
        } else if (pm2_5 >= 10) {
            return ANSI_YELLOW + pm2_5 + ANSI_RESET;
        } else {
            return ANSI_GREEN + pm2_5 + ANSI_RESET;
        }
    }

    //
    private String getPM10(Double pm10) {
        if ( pm10 >= 50) {
            return ANSI_RED + pm10 + ANSI_RESET;
        } else if (pm10 >= 20) {
            return ANSI_YELLOW + pm10 + ANSI_RESET;
        } else {
            return ANSI_GREEN + pm10 + ANSI_RESET;
        }
    }
}