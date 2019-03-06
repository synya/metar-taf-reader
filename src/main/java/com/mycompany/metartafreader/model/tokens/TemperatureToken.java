package com.mycompany.metartafreader.model.tokens;

import com.mycompany.metartafreader.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TemperatureToken extends AbstractToken {

    private static final Pattern TEMPERATURE = Pattern.compile("^(?<temperature>M?[0-9]{2})/(?<dewPoint>M?[0-9]{2})$");

    private final List<Temperature> temperatures = new ArrayList<>();

    public final List<Temperature> getTemperatures() {
        return temperatures;
    }

    @Override
    public final String toString() {
        return "TemperatureToken{" +
                "temperatures=" + temperatures +
                '}';
    }

    @Override
    protected final boolean mustBeJoinedBeforeDecode() {
        return false;
    }

    @Override
    protected final Matcher getMatcher(String input) {
        return TEMPERATURE.matcher(input);
    }

    @Override
    protected final void runDecode(String token) {
        Matcher matcher = TEMPERATURE.matcher(token);
        if (matcher.find()) {
            temperatures.add(new Temperature(matcher.group("temperature"), matcher.group("dewPoint")));
        }
    }

    public static final class Temperature {

        private final String temperature;
        private final String dewPoint;

        public Temperature(String temperature, String dewPoint) {
            this.temperature = StringUtil.nullableConverter(temperature, t -> t.replace("M", "-"));
            this.dewPoint = StringUtil.nullableConverter(dewPoint, dp -> dp.replace("M", "-"));
        }

        public final String getTemperature() {
            return temperature;
        }

        public final String getDewPoint() {
            return dewPoint;
        }

        @Override
        public final String toString() {
            return "Temperature{" +
                    "temperature='" + temperature + '\'' +
                    ", dewPoint='" + dewPoint + '\'' +
                    '}';
        }
    }

}

