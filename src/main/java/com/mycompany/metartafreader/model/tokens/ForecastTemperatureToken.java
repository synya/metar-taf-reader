package com.mycompany.metartafreader.model.tokens;

import com.mycompany.metartafreader.utils.StringUtil;
import com.mycompany.metartafreader.utils.TokenDateTimeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ForecastTemperatureToken extends AbstractToken {

    private static final Pattern TEMPERATURE = Pattern.compile("^T(?<extremum>[XN])(?<temperature>M?[0-9]{2})/(?<dayHour>[0-9]{4})Z$");

    private final List<Temperature> temperatures = new ArrayList<>();

    public final List<Temperature> getTemperatures() {
        return temperatures;
    }

    @Override
    public String toString() {
        return "ForecastTemperatureToken{" +
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
            if (matcher.group("extremum").equals("X")) {
                temperatures.add(new Temperature(matcher.group("temperature"), "maximum", matcher.group("dayHour")));
            } else {
                temperatures.add(new Temperature(matcher.group("temperature"), "minimum", matcher.group("dayHour")));
            }
        }
    }

    public static final class Temperature {

        private final String temperature;
        private final String extremum;
        private final String remark;
        private final LocalDateTime dateTime;

        public Temperature(String temperature, String extremum, String dayHour) {
            this.temperature = StringUtil.nullableConverter(temperature, t -> t.replace("M", "-"));
            this.extremum = StringUtil.nullableConverter(extremum, r -> r);
            this.dateTime = TokenDateTimeUtil.parseDayHour(StringUtil.nullableConverter(dayHour, dh -> dh, () -> "0000"));
            this.remark = String.join(" ", this.extremum, "at:", TokenDateTimeUtil.format(dateTime));
        }

        public String getTemperature() {
            return temperature;
        }

        public String getExtremum() {
            return extremum;
        }

        public String getRemark() {
            return remark;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        @Override
        public final String toString() {
            return "Temperature{" +
                    "temperature='" + temperature + '\'' +
                    ", remark='" + remark + '\'' +
                    '}';
        }
    }
}
