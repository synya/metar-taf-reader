package com.mycompany.metartafreader.model.tokens;

import java.util.*;
import java.util.regex.Matcher;

public abstract class AbstractWeatherPhenomenaToken extends AbstractToken {

    private static final String INTENSITY_GROUP_REGEX = "([+-])";
    private static final String PHENOMENA_GROUP_REGEX = "(VC|MI|BC|PR|DR|BL|SH|TS|FZ|DZ|RA|SN|SG|IC|PL|GR|GS|UP|BR|FG|FU|VA|DU|SA|HZ|PO|SQ|FC|SS|DS)";
    private static final int PHENOMENA_GROUP_INDEX = 1;
    static final String PHENOMENA_REGEX = INTENSITY_GROUP_REGEX + "?" + PHENOMENA_GROUP_REGEX + PHENOMENA_GROUP_REGEX + "?" + PHENOMENA_GROUP_REGEX + "?";

    private static final Map<String, String> WEATHER_PHENOMENA_DICTIONARY = new HashMap<>();

    static {
        WEATHER_PHENOMENA_DICTIONARY.put("+", "heavy");
        WEATHER_PHENOMENA_DICTIONARY.put("-", "light");
        WEATHER_PHENOMENA_DICTIONARY.put(null, "moderate");
        WEATHER_PHENOMENA_DICTIONARY.put("VC", "in the vicinity of airport");
        WEATHER_PHENOMENA_DICTIONARY.put("BC", "patches");
        WEATHER_PHENOMENA_DICTIONARY.put("BL", "blowing");
        WEATHER_PHENOMENA_DICTIONARY.put("BR", "mist");
        WEATHER_PHENOMENA_DICTIONARY.put("DR", "low drifting");
        WEATHER_PHENOMENA_DICTIONARY.put("DS", "dust storm");
        WEATHER_PHENOMENA_DICTIONARY.put("DU", "wide-spread dust");
        WEATHER_PHENOMENA_DICTIONARY.put("DZ", "drizzle");
        WEATHER_PHENOMENA_DICTIONARY.put("FG", "fog");
        WEATHER_PHENOMENA_DICTIONARY.put("FC", "funnel clouds");
        WEATHER_PHENOMENA_DICTIONARY.put("FU", "smoke");
        WEATHER_PHENOMENA_DICTIONARY.put("FZ", "freezing");
        WEATHER_PHENOMENA_DICTIONARY.put("GR", "hail");
        WEATHER_PHENOMENA_DICTIONARY.put("GS", "small hail and/or snow pellets");
        WEATHER_PHENOMENA_DICTIONARY.put("HZ", "haze");
        WEATHER_PHENOMENA_DICTIONARY.put("IC", "ice crystals");
        WEATHER_PHENOMENA_DICTIONARY.put("MI", "shallow");
        WEATHER_PHENOMENA_DICTIONARY.put("PL", "ice pellets");
        WEATHER_PHENOMENA_DICTIONARY.put("PO", "dust/sand whirls");
        WEATHER_PHENOMENA_DICTIONARY.put("PR", "partial");
        WEATHER_PHENOMENA_DICTIONARY.put("RA", "rain");
        WEATHER_PHENOMENA_DICTIONARY.put("SA", "sand");
        WEATHER_PHENOMENA_DICTIONARY.put("SG", "snow grains");
        WEATHER_PHENOMENA_DICTIONARY.put("SH", "shower");
        WEATHER_PHENOMENA_DICTIONARY.put("SN", "snow");
        WEATHER_PHENOMENA_DICTIONARY.put("SQ", "squalls");
        WEATHER_PHENOMENA_DICTIONARY.put("SS", "sandstorm");
        WEATHER_PHENOMENA_DICTIONARY.put("TS", "thunderstorm");
        WEATHER_PHENOMENA_DICTIONARY.put("VA", "volcanic ash");
        WEATHER_PHENOMENA_DICTIONARY.put("UP", "unknown precipitation");
        WEATHER_PHENOMENA_DICTIONARY.put("", "");
    }

    private List<String> phenomena = new ArrayList<>();

    public final List<String> getPhenomena() {
        return phenomena;
    }

    @Override
    protected final void runDecode(String token) {
        Matcher matcher = getMatcher(token);
        if (matcher.find()) {
            for (int i = PHENOMENA_GROUP_INDEX; i <= matcher.groupCount(); i++) {
                if ((matcher.group(i) != null) && (WEATHER_PHENOMENA_DICTIONARY.get(matcher.group(i)) != null)) {
                    phenomena.add(WEATHER_PHENOMENA_DICTIONARY.get(matcher.group(i)));
                }
            }
        }
    }
}
