package com.mycompany.metartafreader.model.tokens;

import com.mycompany.metartafreader.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class VisibilityToken extends AbstractToken {

    private static final Pattern VISIBILITY = Pattern.compile("^(?<special>CAVOK|////)$|" +
            "^(?<international>(?<metersBound>[PM])?(?<metersDistance>[0-9]{4})(?<direction>NDV|NE|NW|N|SE|SW|S|E)?)$|" +
            "^(?<theUSA>(?<stateMilesBound>[PM])?(?<stateMilesDistance>([0-9]{1,2})SM|([0-9]/[0-9])SM))$");

    private static final Map<String, String> VISIBILITY_DICTIONARY = new HashMap<>();

    static {
        VISIBILITY_DICTIONARY.put("CAVOK", "ceiling and visibility OK");
        VISIBILITY_DICTIONARY.put("////", "visibility is not determined");
        VISIBILITY_DICTIONARY.put("NDV", "no directional visibility");
        VISIBILITY_DICTIONARY.put("N", "north");
        VISIBILITY_DICTIONARY.put("NE", "north-east");
        VISIBILITY_DICTIONARY.put("E", "east");
        VISIBILITY_DICTIONARY.put("SE", "south-east");
        VISIBILITY_DICTIONARY.put("S", "south");
        VISIBILITY_DICTIONARY.put("SW", "south-west");
        VISIBILITY_DICTIONARY.put("W", "west");
        VISIBILITY_DICTIONARY.put("NW", "north-west");
        VISIBILITY_DICTIONARY.put("P", "prevailing");
        VISIBILITY_DICTIONARY.put("M", "less than");
        VISIBILITY_DICTIONARY.put("", "");
    }

    private final List<Visibility> visibilities = new ArrayList<>();

    public final List<Visibility> getVisibilities() {
        return visibilities;
    }

    @Override
    public final String toString() {
        return "VisibilityToken{" +
                "visibilities=" + visibilities +
                '}';
    }

    @Override
    protected boolean mustBeJoinedBeforeDecode() {
        return false;
    }

    @Override
    protected final Matcher getMatcher(String input) {
        return VISIBILITY.matcher(input);
    }

    @Override
    protected final void runDecode(String token) {
        Matcher matcher = VISIBILITY.matcher(token);
        if (matcher.find()) {
            if (matcher.group("special") != null) {
                visibilities.add(new Visibility(matcher.group("special")));
            }
            if (matcher.group("international") != null) {
                visibilities.add(new Visibility(matcher.group("direction"), matcher.group("metersDistance"),
                        "meters", matcher.group("metersBound")));
            }
            if (matcher.group("theUSA") != null) {
                visibilities.add(new Visibility("", matcher.group("stateMilesDistance"),
                        "state miles", matcher.group("stateMilesBound")));
            }
        }
    }

    public static final class Visibility {

        private final String direction;
        private final String remark;
        private final String distance;
        private final String unit;

        public Visibility(String remark) {
            this(null, null, null, remark);
        }

        public Visibility(String direction, String distance, String unit, String remark) {
            this.direction = StringUtil.nullableConverter(direction, VISIBILITY_DICTIONARY::get);
            this.distance = StringUtil.nullableConverter(distance, d -> d.replace("SM", ""));
            this.unit = StringUtil.nullableConverter(unit, u -> u);
            this.remark = StringUtil.nullableConverter(remark, VISIBILITY_DICTIONARY::get);
        }

        public String getDirection() {
            return direction;
        }

        public String getRemark() {
            return remark;
        }

        public String getDistance() {
            return distance;
        }

        public String getUnit() {
            return unit;
        }

        @Override
        public String toString() {
            return "Visibility{" +
                    "direction='" + direction + '\'' +
                    ", remark='" + remark + '\'' +
                    ", distance='" + distance + '\'' +
                    ", unit='" + unit + '\'' +
                    '}';
        }
    }
}
