package com.mycompany.metartafreader.model.tokens;

import com.mycompany.metartafreader.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class CloudToken extends AbstractToken {

    private static final int TYPE_GROUP_INDEX = 1;
    private static final int FLIGHT_LEVEL_GROUP_INDEX = 2;
    private static final int REMARK_GROUP_INDEX = 3;
    private static final Pattern CLOUDS = Pattern.compile("^(NSC|NCD|FEW|SCT|BKN|OVC|VV)([0-9/]{3})?(CB|TCU|///)?$");

    private static final Map<String, String> CLOUDS_DICTIONARY = new HashMap<>();

    static {
        CLOUDS_DICTIONARY.put("NSC", "no significant clouds");
        CLOUDS_DICTIONARY.put("NCD", "no clouds detected");
        CLOUDS_DICTIONARY.put("FEW", "few (1-2 octant)");
        CLOUDS_DICTIONARY.put("SCT", "scattered (3-4 octant)");
        CLOUDS_DICTIONARY.put("BKN", "broken (5-7 octant)");
        CLOUDS_DICTIONARY.put("OVC", "overcast (8 octant)");
        CLOUDS_DICTIONARY.put("VV", "vertical visibility");
        CLOUDS_DICTIONARY.put("CB", "cumulonimbus");
        CLOUDS_DICTIONARY.put("TCU", "towering cumulus");
        CLOUDS_DICTIONARY.put("///", "cannot be detected");
    }

    private final List<Cloud> clouds = new ArrayList<>();

    public List<Cloud> getClouds() {
        return clouds;
    }

    @Override
    public String toString() {
        return "CloudToken{" +
                "clouds=" + clouds +
                '}';
    }

    @Override
    protected boolean mustBeJoinedBeforeDecode() {
        return false;
    }

    @Override
    protected Matcher getMatcher(String input) {
        return CLOUDS.matcher(input);
    }

    @Override
    protected void runDecode(String token) {
        Matcher matcher = CLOUDS.matcher(token);
        if (matcher.find()) {
            clouds.add(new Cloud(matcher.group(TYPE_GROUP_INDEX), matcher.group(FLIGHT_LEVEL_GROUP_INDEX), matcher.group(REMARK_GROUP_INDEX)));
        }
    }

    public static class Cloud {

        private final String type;
        private final String flightLevel;
        private final String remark;

        public Cloud(String type, String flightLevel, String remark) {
            this.type = CLOUDS_DICTIONARY.get(type);
            this.flightLevel = StringUtil.nullableConverter(flightLevel, fl -> fl.contains("/") ? CLOUDS_DICTIONARY.get(fl) : fl);
            this.remark = StringUtil.nullableConverter(remark, CLOUDS_DICTIONARY::get);
        }

        public String getType() {
            return type;
        }

        public String getFlightLevel() {
            return flightLevel;
        }

        public String getRemark() {
            return remark;
        }

        @Override
        public String toString() {
            return "Cloud{" +
                    "type='" + type + '\'' +
                    ", flightLevel='" + flightLevel + '\'' +
                    ", remark='" + remark + '\'' +
                    '}';
        }
    }
}
