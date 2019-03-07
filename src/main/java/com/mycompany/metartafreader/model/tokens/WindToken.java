package com.mycompany.metartafreader.model.tokens;

import com.mycompany.metartafreader.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WindToken extends AbstractToken {

    private static final Pattern WIND = Pattern.compile("^(?<wind>(?<windDirection>[0-9]{3})(?<windSpeed>[0-9]{1,2})" +
            "(?<windGustsSpeed>G[0-9]{1,2})?(?<windSpeedUnit>MPS|KT|KMH))$|" +
            "^(?<variableSlowWind>VRB(?<variableSlowWindSpeed>([0-9]{2}))(?<variableSlowWindSpeedUnit>MPS|KT|KMH))$|" +
            "^(?<variableHardWind>(?<variableHardWindDirection1>[0-9]{3})V(?<variableHardWindDirection2>[0-9]{3}))$");

    private static final Map<String, String> UNITS_DICTIONARY = new HashMap<>();

    static {
        UNITS_DICTIONARY.put("MPS", "meters per second");
        UNITS_DICTIONARY.put("KT", "knots");
        UNITS_DICTIONARY.put("KMH", "kilometers per hour");
        UNITS_DICTIONARY.put("", "");
    }

    private final List<Wind> winds = new ArrayList<>();

    public final List<Wind> getWinds() {
        return winds;
    }

    @Override
    public final String toString() {
        return "WindToken{" +
                "winds=" + winds +
                '}';
    }

    @Override
    protected boolean mustBeJoinedBeforeDecode() {
        return false;
    }

    @Override
    protected final Matcher getMatcher(String input) {
        return WIND.matcher(input);
    }

    @Override
    protected final void runDecode(String token) {
        Matcher matcher = WIND.matcher(token);
        if (matcher.find()) {
            if (matcher.group("wind") != null) {
                winds.add(new Wind(matcher.group("windSpeed"), matcher.group("windGustsSpeed"),
                        matcher.group("windSpeedUnit"), matcher.group("windDirection")));
            }
            if (matcher.group("variableSlowWind") != null) {
                winds.add(new Wind(matcher.group("variableSlowWindSpeed"), null,
                        matcher.group("variableSlowWindSpeedUnit"), "variable"));
            }
            if (matcher.group("variableHardWind") != null) {
                winds.add(new Wind(null, null, null,
                        matcher.group("variableHardWindDirection1"), matcher.group("variableHardWindDirection2")));
            }
        }
    }

    public static final class Wind {
        private final String[] directions;
        private final String speed;
        private final String gusts;
        private final String unit;


        public Wind(String speed, String gusts, String unit, String... directions) {
            Assert.notNull(directions, "directions must not be null");
            this.directions = directions;
            this.speed = StringUtil.nullableConverter(speed, s -> s);
            this.gusts = StringUtil.nullableConverter(gusts, g -> g.replace("G", ""));
            this.unit = StringUtil.nullableConverter(unit, UNITS_DICTIONARY::get);
        }

        public String[] getDirections() {
            return directions;
        }

        public String getSpeed() {
            return speed;
        }

        public String getGusts() {
            return gusts;
        }

        public String getUnit() {
            return unit;
        }

        @Override
        public String toString() {
            return "Wind{" +
                    "directions=" + Arrays.toString(directions) +
                    ", speed='" + speed + '\'' +
                    ", gusts='" + gusts + '\'' +
                    ", unit='" + unit + '\'' +
                    '}';
        }
    }
}
