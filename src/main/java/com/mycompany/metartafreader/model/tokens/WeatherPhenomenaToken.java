package com.mycompany.metartafreader.model.tokens;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WeatherPhenomenaToken extends AbstractWeatherPhenomenaToken {

    private static final Pattern RECENT_WEATHER = Pattern.compile("^" + PHENOMENA_REGEX + "$");

    @Override
    protected final boolean mustBeJoinedBeforeDecode() {
        return false;
    }

    @Override
    protected final Matcher getMatcher(String input) {
        return RECENT_WEATHER.matcher(input);
    }
}
