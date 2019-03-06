package com.mycompany.metartafreader.model.tokens;

import com.mycompany.metartafreader.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RunwayVisibilityToken extends AbstractToken {

    private static final Pattern RUNWAY_VISIBILITY = Pattern.compile("^(?<runway>R[0-9]{2}[LCR]?)/" +
            "(?<bound>[MP])?(?<distance>[0-9]{4})(?<unit>FT/?)?(?<tendency>[NUD])?$");

    private static final Map<String, String> RUNWAY_VISIBILITY_DICTIONARY = new HashMap<>();

    static {
        RUNWAY_VISIBILITY_DICTIONARY.put("P", "prevailing");
        RUNWAY_VISIBILITY_DICTIONARY.put("M", "less than");
        RUNWAY_VISIBILITY_DICTIONARY.put("N", "without tendency to change");
        RUNWAY_VISIBILITY_DICTIONARY.put("U", "tendency to upwards");
        RUNWAY_VISIBILITY_DICTIONARY.put("D", "tendency to downwards");
        RUNWAY_VISIBILITY_DICTIONARY.put("FT", "feet");
        RUNWAY_VISIBILITY_DICTIONARY.put("FT/", "feet");
    }

    private final List<RunwayVisibility> runwayVisibilities = new ArrayList<>();

    public final List<RunwayVisibility> getRunwayVisibilities() {
        return runwayVisibilities;
    }

    @Override
    public final String toString() {
        return "RunwayVisibilityToken{" +
                "runwayVisibilities=" + runwayVisibilities +
                '}';
    }

    @Override
    protected boolean mustBeJoinedBeforeDecode() {
        return false;
    }

    @Override
    protected Matcher getMatcher(String input) {
        return RUNWAY_VISIBILITY.matcher(input);
    }

    @Override
    protected void runDecode(String token) {
        Matcher matcher = RUNWAY_VISIBILITY.matcher(token);
        if (matcher.find()) {
            runwayVisibilities.add(new RunwayVisibility(matcher.group("runway"), matcher.group("bound"),
                    matcher.group("distance"), matcher.group("unit"), matcher.group("tendency")));
        }
    }

    public static final class RunwayVisibility {
        private final String runway;
        private final String bound;
        private final String distance;
        private final String unit;
        private final String tendency;

        public RunwayVisibility(String runway, String bound, String distance, String unit, String tendency) {
            this.runway = StringUtil.nullableConverter(runway, r -> r.substring(1));
            this.bound = StringUtil.nullableConverter(bound, RUNWAY_VISIBILITY_DICTIONARY::get);
            this.distance = StringUtil.nullableConverter(distance, d -> d);
            this.unit = StringUtil.nullableConverter(unit, RUNWAY_VISIBILITY_DICTIONARY::get, () -> "meters");
            this.tendency = StringUtil.nullableConverter(tendency, RUNWAY_VISIBILITY_DICTIONARY::get);
        }

        public final String getRunway() {
            return runway;
        }

        public final String getBound() {
            return bound;
        }

        public final String getDistance() {
            return distance;
        }

        public final String getUnit() {
            return unit;
        }

        public final String getTendency() {
            return tendency;
        }

        @Override
        public final String toString() {
            return "RunwayVisibility{" +
                    "runway='" + runway + '\'' +
                    ", bound='" + bound + '\'' +
                    ", distance='" + distance + '\'' +
                    ", unit='" + unit + '\'' +
                    ", tendency='" + tendency + '\'' +
                    '}';
        }
    }
}
