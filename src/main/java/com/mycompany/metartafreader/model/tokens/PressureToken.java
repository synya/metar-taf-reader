package com.mycompany.metartafreader.model.tokens;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PressureToken extends AbstractToken {

    private static final Pattern PRESSURE = Pattern.compile("^(?<standard>(?<standardType>[QA])(?<standardValue>[0-9]{4}))$|" +
            "^(?<local>(?<localType>QFE|SLP)(?<localValue>[0-9]{3}))(?<localOptional>/(?<localOptionalValue>[0-9]{4}))?$");

    private final List<Pressure> pressures = new ArrayList<>();

    public final List<Pressure> getPressures() {
        return pressures;
    }

    @Override
    public final String toString() {
        return "PressureToken{" +
                "pressures=" + pressures +
                '}';
    }

    @Override
    protected final boolean mustBeJoinedBeforeDecode() {
        return false;
    }

    @Override
    protected final Matcher getMatcher(String input) {
        return PRESSURE.matcher(input);
    }

    @Override
    protected final void runDecode(String token) {
        Matcher matcher = PRESSURE.matcher(token);
        if (matcher.find()) {
            if (matcher.group("standard") != null) {
                if (matcher.group("standardType").equals("Q")) {
                    pressures.add(new Pressure(matcher.group("standardValue"), "hPa", "QNH"));
                } else {
                    pressures.add(new Pressure(matcher.group("standardValue"), "inches of mercury", "QNH"));
                }
            }
            if (matcher.group("local") != null) {
                String value = matcher.group("localValue");
                if (matcher.group("localType").equals("SLP")) {
                    pressures.add(new Pressure("10" + value.substring(0, 2) + "." + value.substring(2),
                            "mBar", "sea level pressure"));
                } else {
                    pressures.add(new Pressure(value, "mm of mercury", "QFE"));
                    if (matcher.group("localOptional") != null) {
                        pressures.add(new Pressure(matcher.group("localOptionalValue"), "hPa", "QFE"));
                    }
                }
            }
        }
    }

    public static class Pressure {
        private final String pressure;
        private final String units;
        private final String remark;

        public Pressure(String pressure, String units, String remark) {
            this.pressure = pressure;
            this.units = units;
            this.remark = remark;
        }

        public final String getPressure() {
            return pressure;
        }

        public final String getUnits() {
            return units;
        }

        public final String getRemark() {
            return remark;
        }

        @Override
        public final String toString() {
            return "Pressure{" +
                    "pressure='" + pressure + '\'' +
                    ", units='" + units + '\'' +
                    ", remark='" + remark + '\'' +
                    '}';
        }
    }
}
